package life.macchiato.youtube.models;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.SearchResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Entity
@Getter
@ToString
@Table(name = "VIDEO_RESULT")
public class VideoResult extends SearchListResult {

    private static final String BASE_VIDEO_URL = "https://www.youtube.com/watch?v=";

    private String videoId;
    private String webpageUrl;
    private LocalDateTime publishDate;

    public VideoResult() {
    }

    public VideoResult(builder b) {
        super(b);
        videoId = b.videoId;
        webpageUrl = BASE_VIDEO_URL.concat(videoId);
        publishDate = b.publishDate;
    }

    public static class builder extends SearchListResult.builder<builder>{

        private final String videoId;
        private LocalDateTime publishDate;

        public builder(String videoId) {
            this.videoId = Objects.requireNonNull(videoId);
        }

        public builder publishDate(DateTime publishedAt) {
            Instant instant = Instant.ofEpochMilli(publishedAt.getValue());
            publishDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return this;
        }

        @Override
        public builder self() { return this; }

        @Override
        public VideoResult build() {
            return new VideoResult(this);
        }
    }

    public static VideoResult from(SearchResult result)
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
}

