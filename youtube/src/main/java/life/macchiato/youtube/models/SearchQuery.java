package life.macchiato.youtube.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "Queries")
@Table(name = "queries")
public class SearchQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "query_id")
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "page_token")
    private String pageToken;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    public SearchQuery(String value, String pageToken) {
        this.value = value;
        this.pageToken = pageToken;
    }
}