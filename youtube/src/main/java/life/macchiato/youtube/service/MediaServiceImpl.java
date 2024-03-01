package life.macchiato.youtube.service;

import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import life.macchiato.youtube.model.*;
import life.macchiato.youtube.repository.VideoRequestRepository;
import life.macchiato.youtube.repository.YoutubeItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static life.macchiato.youtube.util.GoogleApiUtil.*;


@Slf4j
@Service
@AllArgsConstructor
public class MediaServiceImpl implements MediaService {

    YoutubeItemRepository itemRepo;
    VideoRequestRepository requestRepo;

    private static final String VIDEO_KIND = "youtube#video";
    private static final String PLAYLIST_KIND = "youtube#playlist";

    @Override
    public List<YoutubeItem> allYoutubeItems(String videoTitle) throws GeneralSecurityException, IOException {
        log.info("new video request {}", videoTitle);

        Optional<VideoRequest> byName = requestRepo.findByName(videoTitle);
        if (byName.isPresent())
        {
            return byName.get().getItems();
        }

        SearchListResponse searchList = getYoutubeService()
            .search()
            .list("snippet")
            .setQ(videoTitle)
            .execute();

        List<YoutubeItem> items = new ArrayList<>();

        for(SearchResult result : searchList.getItems())
        {
             YoutubeItem item = switch (result.getId().getKind())
            {
                case VIDEO_KIND -> YoutubeVideo.from(result);
                case PLAYLIST_KIND -> YoutubePlaylist.from(result);
                default -> null;
            };

             if (item == null) continue;

             items.add(itemRepo.findByItemId(item.getItemId())
                     .orElse(item));
        }


        itemRepo.saveAll(items);
        requestRepo.save(VideoRequest.builder()
                .name(videoTitle)
                .items(items)
                .build());
        return items;
    }
}
