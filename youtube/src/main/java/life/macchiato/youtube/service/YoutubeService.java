package life.macchiato.youtube.service;

import life.macchiato.youtube.dto.CreateSearchDTO;
import life.macchiato.youtube.model.VideoRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public interface YoutubeService {
    void createSearch(CreateSearchDTO searchDTO) throws GeneralSecurityException, IOException;

    List<VideoRequest> showRequests();
}
