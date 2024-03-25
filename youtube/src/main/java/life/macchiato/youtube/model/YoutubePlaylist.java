package life.macchiato.youtube.model;

import com.google.api.services.youtube.model.SearchResult;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
@Entity
@Getter
@ToString
@NoArgsConstructor
public class YoutubePlaylist extends YoutubeItem {

    private static final String baseUrl = "https://www.youtube.com/watch?v=&list=";

    public YoutubePlaylist(builder b) {
        super(b);
    }
    public static class builder extends YoutubeItem.builder<builder, YoutubePlaylist> {

        @Override
        protected builder self() {
            return this;
        }

        @Override
        YoutubePlaylist build() {
            return new YoutubePlaylist(this);
        }
    }
    public static YoutubePlaylist from(SearchResult result) {
        return new YoutubePlaylist.builder()
                .title(result.getSnippet().getTitle())
                .itemId(result.getId().getPlaylistId())
                .url(baseUrl.concat(result.getId().getPlaylistId()))
                .publishDate(result.getSnippet().getPublishedAt())
                .thumbnail(result.getSnippet().getThumbnails().getHigh().getUrl())
                .build();
    }

}
