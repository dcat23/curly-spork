package life.macchiato.youtube.models;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "SEARCH_RESULT")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class SearchListResult {

    @Id
    @SequenceGenerator(
            name = "result_id_sequence",
            sequenceName = "result_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "result_id_sequence"
    )
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    String etag;
    String title;
    String thumbnail;

    public SearchListResult() {
    }

    SearchListResult(builder<?> builder) {
        etag = builder.etag;
        title = builder.title;
        thumbnail = builder.thumbnail;
    }
    abstract static class builder<T extends builder<T>> {
        private String title;
        private String thumbnail;
        private String etag;

        public builder() {}

        protected abstract T self();
        abstract VideoResult build();

        public T title(String title) {
            this.title = title;
            return self();
        }
        public T thumbnail(String val) {
            thumbnail = val;
            return self();
        }
        public T etag(String val) {
            etag = val;
            return self();
        }

    }
}