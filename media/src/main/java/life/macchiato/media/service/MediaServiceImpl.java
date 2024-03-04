package life.macchiato.media.service;

import com.jfposton.ytdlp.YtDlp;
import com.jfposton.ytdlp.YtDlpException;
import com.jfposton.ytdlp.mapper.VideoInfo;
import life.macchiato.media.dto.DownloadRequest;
import life.macchiato.media.dto.DownloadStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MediaServiceImpl implements MediaService {



    @Override
    public DownloadStatus status(long id) {
        return new DownloadStatus();
    }

    @Override
    public void requestVideo(DownloadRequest request) throws YtDlpException {
        log.info("New download request {}", request);

        VideoInfo videoInfo = YtDlp.getVideoInfo(request.url());
//        Media video = Media.from(videoInfo);
        log.info("{}", videoInfo);
    }
}
