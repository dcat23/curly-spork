package life.macchiato.media.util;

import brave.internal.Nullable;
import com.jfposton.ytdlp.YtDlp;
import com.jfposton.ytdlp.YtDlpException;
import com.jfposton.ytdlp.YtDlpRequest;
import com.jfposton.ytdlp.YtDlpResponse;
import life.macchiato.media.model.Media;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.OutputStream;
import java.util.Optional;

@Slf4j
public class Downloader {

    private final YtDlpRequest request;
    private final Progress progress;
    private YtDlpResponse response;

    public Downloader(builder builder) {
        request = builder.request;
        progress = new Progress();
        response = null;
    }
    public static class builder {

        private final YtDlpRequest request;

        public builder(String url) {
            request = new YtDlpRequest(url);
        }

        public Downloader build() {
            return new Downloader(this);
        }

        public builder format(String extension) {
            request.setOption("format", extension);
            return this;
        }

        public builder directory(String... pathSegments) {
            String homeDir = System.getProperty("user.home");
            String directory = UriComponentsBuilder.fromPath(homeDir)
                    .pathSegment(pathSegments)
                    .build()
                    .toUriString();

            File fileDirectory = new File(directory);
            if (!fileDirectory.exists())
            {
                if (fileDirectory.mkdirs()) {
                    log.info("created directory {}", directory);
                }
                else
                {
                    log.info("error creating directory {}", directory);
                    return this;
                }
            }
            request.setDirectory((directory));
            return this;
        }
    }

    @Async("downloadExecutor")
    public void execute(@Nullable OutputStream outputStream)
    {
        try
        {
            response = YtDlp.execute(request, outputStream,  progress);
        }
        catch (YtDlpException e)
        {
            throw new RuntimeException(e);
        }

    }

    @Async("downloadExecutor")
    public void execute()
    {
        execute(null);
    }


    public Optional<YtDlpResponse> getResponse() {
        return Optional.of(response);
    }

}
