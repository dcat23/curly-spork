package life.macchiato.youtube.model;

import com.google.api.services.youtube.model.SearchResult;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class YoutubeVideo extends YoutubeItem {

    private static final String baseUrl = "https://www.youtube.com/watch?v=";

    public YoutubeVideo(builder b) {
        super(b);
    }

    public static class builder extends YoutubeItem.builder<builder, YoutubeVideo> {

        @Override
        protected builder self() {
            return this;
        }

        @Override
        YoutubeVideo build() {
            return new YoutubeVideo(this);
        }
    }

    public static YoutubeVideo from(SearchResult result) {
        return new YoutubeVideo.builder()
                .title(result.getSnippet().getTitle())
                .itemId(result.getId().getVideoId())
                .url(baseUrl.concat(result.getId().getVideoId()))
                .publishDate(result.getSnippet().getPublishedAt())
                .thumbnail(result.getSnippet().getThumbnails().getHigh().getUrl())
                .build();
    }
}
