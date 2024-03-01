package life.macchiato.youtube.model;

import com.google.api.client.util.DateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "YOUTUBE_RESULT")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class YoutubeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String url;
    String title;
    String itemId;
    String thumbnail;
    LocalDateTime publishDate;

    YoutubeItem(builder<?,?> b) {
        url = b.url;
        title = b.title;
        itemId = b.itemId;
        thumbnail = b.thumbnail;
        publishDate = b.publishDate;
    }

    abstract static class builder<
            B extends builder<B, C>,
            C extends YoutubeItem>
    {
        private String url;
        private String title;
        private String itemId;
        private String thumbnail;
        private LocalDateTime publishDate;


        public builder() {}

        public B url(String url) {
            this.url = url;
            return self();
        }
        public B thumbnail(String val) {
            thumbnail = val;
            return self();
        }
        public B title(String val) {
            title = val;
            return self();
        }
        public B itemId(String val) {
            itemId = Objects.requireNonNull(val);
            return self();
        }
        public B publishDate(DateTime publishedAt) {
            Instant instant = Instant.ofEpochMilli(publishedAt.getValue());
            publishDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return self();
        }

        protected abstract B self();
        abstract C build();
    }
}
