package life.macchiato.media.model;

import jakarta.persistence.*;
import life.macchiato.media.dto.DownloadStatus;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String ext;
    private String videoId;
    @Column(unique = true)
    private String url;
    @Enumerated
    private DownloadStatus status;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
}
