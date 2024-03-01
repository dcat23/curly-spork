package life.macchiato.youtube.model;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.SearchResult;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@NoArgsConstructor
public class YoutubeVideo extends YoutubeItem {

    private static final String baseUrl = "https://www.youtube.com/watch?v=";

    private LocalDateTime publishDate;

    public YoutubeVideo(builder b) {
        super(b);
        publishDate = b.publishDate;
    }

    public static class builder extends YoutubeItem.builder<builder, YoutubeVideo> {
        private LocalDateTime publishDate;

        public builder publishDate(DateTime publishedAt) {
            Instant instant = Instant.ofEpochMilli(publishedAt.getValue());
            publishDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return this;
        }

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
