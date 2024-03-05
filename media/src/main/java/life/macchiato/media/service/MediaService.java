package life.macchiato.media.service;


import life.macchiato.media.dto.MediaRequest;
import life.macchiato.media.dto.DownloadStatus;
import life.macchiato.media.exception.ResourceNotFound;
import life.macchiato.media.model.Media;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public interface MediaService {
    DownloadStatus status(long id) throws ResourceNotFound;

    CompletableFuture<Media> requestVideo(MediaRequest mediaRequest) throws Exception;
}
