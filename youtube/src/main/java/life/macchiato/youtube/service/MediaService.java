package life.macchiato.youtube.service;

import life.macchiato.youtube.model.YoutubeItem;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public interface MediaService {
    List<YoutubeItem> allYoutubeItems(String videoTitle) throws GeneralSecurityException, IOException;
}
