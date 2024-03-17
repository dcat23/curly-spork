package life.macchiato.courses.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import pro.macchiato.cli.Cli;
import pro.macchiato.cli.ProgressCallback;
import pro.macchiato.cli.exceptions.CliException;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
public class TransCli extends Cli {

    private final String torrentFile;

    public TransCli(String torrentFile) {
        super("transmission-cli");
        this.torrentFile = torrentFile;
        addOption(torrentFile);
        setOutDirectory("trans");
    }

    public void setOutDirectory(String... paths) {
        String outDir = UriComponentsBuilder.fromPath(this.directory)
                .pathSegment(paths)
                .build()
                .toUriString();

        File fileDirectory = new File(outDir);
        if (!fileDirectory.exists())
        {
            if (fileDirectory.mkdirs()) {
                log.info("created directory {}", outDir);
            }
            else
            {
                log.info("error creating outDir {}", outDir);
            }
        }
        addOption("-w", outDir);
    }

    @Override
    public void execute() throws CliException {
        execute(new Callback());
    }

    public static class Callback implements ProgressCallback {

        private static final String GROUP_PERCENT = "percent";
        private static final String GROUP_RUNTIME = "runtime";
        private static final String GROUP_PEERS = "peers";

        private final Instant start = Instant.now();

        @Getter
        private float progress;
        @Getter
        private float runtime;
        @Getter
        private String peers;
        private final Pattern p = Pattern.compile(
                "Progress:\\s+(?<percent>\\d+\\.\\d+)%.*from\\s+(?<peers>\\d+\\sof\\s\\d+).*\\[(?<runtime>\\d+\\.\\d+)]");
        @Override
        public void processLine(String line) {
            Matcher m = p.matcher(line);
            if (m.matches()) {
                float progress = Float.parseFloat(m.group(GROUP_PERCENT));
                float runtime = Float.parseFloat(m.group(GROUP_RUNTIME));
                String peers = m.group(GROUP_PEERS);
                if (progress != this.progress
                        || runtime != this.runtime
                        || !peers.equals(this.peers))
                {
                    log.info(line);
                    this.progress = progress;
                    this.runtime = runtime;
                    this.peers = peers;
                }
            }
        }

        @Override
        public long getElapsed() {
            Instant now = Instant.now();
            Duration duration = Duration.between(start, now);
            return duration.toSeconds();
        }
    }
}
