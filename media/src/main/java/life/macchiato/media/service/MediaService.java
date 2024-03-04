package life.macchiato.media.service;


import com.jfposton.ytdlp.YtDlpException;
import life.macchiato.media.dto.DownloadRequest;
import life.macchiato.media.dto.DownloadStatus;
import org.springframework.stereotype.Service;

@Service
public interface MediaService {
    DownloadStatus status(long id);

    void requestVideo(DownloadRequest request) throws YtDlpException;
}
