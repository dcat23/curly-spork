package life.macchiato.youtube.services;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import life.macchiato.youtube.models.SearchQuery;
import life.macchiato.youtube.models.SearchResponse;
import life.macchiato.youtube.models.VideoResult;
import life.macchiato.youtube.repositories.QueryRepository;
import life.macchiato.youtube.repositories.SearchRepository;
import life.macchiato.youtube.utils.GoogleApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
public class YoutubeService {

    private final SearchRepository searchRepo;

    private final QueryRepository queryRepo;

    public SearchResponse searchList(SearchQuery searchQuery) {

        Optional<SearchQuery> byValue = queryRepo.findByValueAndPageToken(
                searchQuery.getValue(),
                searchQuery.getPageToken());

        if (byValue.isPresent())
        {
            log.info("Found recent searchQuery, {}", byValue.get());

            Optional<SearchResponse> byQuery = searchRepo.findByQueryAndPageToken(
                    searchQuery.getValue(),
                    searchQuery.getPageToken());

            if (byQuery.isPresent())
            {
                return byQuery.get();
            }
        }

        SearchListResponse searchList = null;
        try {
            YouTube youtubeApi = GoogleApiUtil.getYoutubeService();
            searchList = youtubeApi.search().list("snippet")
                    .setPageToken(searchQuery.getPageToken())
                    .setQ(searchQuery.getValue())
                    .execute();
        } catch (GeneralSecurityException | IOException e) {
            log.info("Issue fetching search list: {}", e.getLocalizedMessage());
            return null;
        }

        if (searchList == null) return null;

        List<VideoResult> results = new ArrayList<>();

        for (SearchResult result : searchList.getItems())
        {
            switch(result.getId().getKind())
            {
//                todo: create playlist object
                case "youtube#playlist" -> log.info("playlist {}",result);
                case "youtube#video" -> results.add(from(result));
                default -> log.info("unknown result {}", result);
            }
        }

        SearchResponse response = SearchResponse.builder()
                .etag(searchList.getEtag())
                .nextPageToken(searchList.getNextPageToken() != null ? searchList.getNextPageToken() : "")
                .previousPageToken(searchList.getPrevPageToken() != null ? searchList.getPrevPageToken() : "")
                .pageToken(searchQuery.getPageToken())
                .query(searchQuery.getValue())
                .results(results)
                .build();

//        todo: use a save and flush to have access to the id
        searchRepo.save(response);
        queryRepo.save(searchQuery);
        return response;

    }
    private VideoResult from(SearchResult result)
    {
        final String videoId = result.getId().getVideoId();
        if (videoId == null) return null;
        return new VideoResult.builder(videoId)
                .etag(result.getEtag())
                .title(result.getSnippet().getTitle())
                .publishDate(result.getSnippet().getPublishedAt())
                .thumbnail(result.getSnippet().getThumbnails().getHigh().getUrl())
                .build();
    }

    public String testTokens() {
        String dir = System.getProperty("user.dir");
        String credPath = "tokens/path";
        File credential = new File(credPath);
        log.info("cred? {} dir {}", credential.exists(), dir);

        return "absolute: " + credential.getAbsolutePath();
    }

    public String findByTitle(String title) {
        queryRepo.findByValue(title);

        return null;
    }
}
