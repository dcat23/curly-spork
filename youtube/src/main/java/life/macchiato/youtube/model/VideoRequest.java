package life.macchiato.youtube.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;
    private String pageToken;
    private String nextPageToken;
    private String previousPageToken;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    private List<YoutubeItem> items;
}
