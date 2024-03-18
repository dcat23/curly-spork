package life.macchiato.media.service;


import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import life.macchiato.media.dto.MediaRequest;
import life.macchiato.media.dto.DownloadStatus;
import life.macchiato.media.exception.ResourceNotFound;
import life.macchiato.media.model.Media;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MediaService {

    Media status(long id) throws ResourceNotFound;
    List<Media> allByStatus(@Nullable DownloadStatus status);

    Media requestVideo(MediaRequest mediaRequest) throws Exception;

    @Async("downloadExecutor")
    void execute(Media media);

}
