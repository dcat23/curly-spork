package life.macchiato.media.util;

import com.jfposton.ytdlp.DownloadProgressCallback;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.time.Instant;

@Getter
@Slf4j
public class Progress implements DownloadProgressCallback {

    private final Instant start;
    private float progress;
    private long eta;


    public Progress() {
        start = Instant.now();
        progress = 0;
        eta = 0;
        log.info("Tracking progress");
    }

    @Override
    public void onProgressUpdate(float progress, long eta) {
        this.progress = progress;
        this.eta = eta;

        if (progress == 0) return;
        if (progress == 100)
        {
            log.info("Download complete");
        }


    }
}
