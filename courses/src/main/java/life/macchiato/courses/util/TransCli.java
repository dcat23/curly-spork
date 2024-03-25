package life.macchiato.courses.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import pro.macchiato.cli.Cli;
import pro.macchiato.cli.CliResult;
import pro.macchiato.cli.ProgressCallback;
import pro.macchiato.cli.exceptions.CliException;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
public class TransCli extends Cli {

    private final String torrentFile;
    @Setter
    private String label = "transmission";

    public TransCli(String torrentFile) {
        super("transmission-cli");
        this.torrentFile = Objects.requireNonNull(torrentFile);
        addOption(this.torrentFile);
    }

    public String getOutDirectory() {
        if (this.options.get("-w") == null)
        {
            setOutDirectory("trans");
        }

        return this.options.get("-w");
    }

    public void setOutDirectory(String... paths) {
        String outDirectory = UriComponentsBuilder.fromPath(this.directory)
                .pathSegment(paths)
                .build()
                .toUriString();

        File fileDirectory = new File(outDirectory);
        if (!fileDirectory.exists())
        {
            if (fileDirectory.mkdirs()) {
                log.info("created directory {}", outDirectory);
            }
            else
            {
                log.info("error creating outDirectory {}", outDirectory);
            }
        }
        addOption("-w", outDirectory);
    }

    @Override
    public CliResult execute() throws CliException {
        return execute(new Callback(label));
    }

    public static class Callback implements ProgressCallback {

        private static final String GROUP_PERCENT = "percent";
        private static final String GROUP_RUNTIME = "runtime";
        private static final String GROUP_PEERS = "peers";

        private final Instant start = Instant.now();
        private final String label;
        @Getter
        private int progress;
        private final Pattern p = Pattern.compile(
                "^.*Progress:\\s+(?<percent>\\d+)\\.\\d+%.*from\\s+(?<peers>\\d+\\sof\\s\\d+).*\\[(?<runtime>\\d+\\.\\d+).*$");

        public Callback(String label) {
            this.label = label;
        }

        @Override
        public void processLine(String line) {
            Matcher m = p.matcher(line);
            if (m.matches()) {
                int progress = Integer.parseInt(m.group(GROUP_PERCENT));
                if (progress != this.progress)
                {
                    this.progress = progress;
                    showProgress();
                }
            } else if (line.startsWith("Seeding")){
                this.progress = 100;
                log.info(line);
            } else {
                log.info("other: {}",line);
            }
        }

        public void showProgress() {
            log.info(label + '[' +
                    "progress=" + progress + "%"+
                    ", runtime=" + getElapsed()+
                    ']');
        }

        @Override
        public String getElapsed() {
            Instant now = Instant.now();
            Duration duration = Duration.between(start, now);
            long seconds = duration.getSeconds();
            long absSeconds = Math.abs(seconds);
            long days = absSeconds / (24 * 3600);
            long hours = (absSeconds % (24 * 3600)) / 3600;
            long minutes = ((absSeconds % (24 * 3600)) % 3600) / 60;
            long secs = ((absSeconds % (24 * 3600)) % 3600) % 60;

            StringBuilder builder = new StringBuilder();
            if (seconds < 0) {
                builder.append("-");
            }
            if (days != 0) {
                builder.append(days).append(" days ");
            }
            if (hours != 0) {
                builder.append(hours).append(" hours ");
            }
            if (minutes != 0) {
                builder.append(minutes).append(" minutes ");
            }
            if (secs != 0 || (days == 0 && hours == 0 && minutes == 0)) {
                builder.append(secs).append(" seconds");
            }
            return builder.toString();
        }

        @Override
        public boolean isReady() {
            return progress >= 100;
        }
    }
}
