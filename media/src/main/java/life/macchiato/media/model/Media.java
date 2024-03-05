package life.macchiato.media.model;

import com.jfposton.ytdlp.mapper.VideoInfo;
import jakarta.persistence.*;
import life.macchiato.media.dto.DownloadStatus;
import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

}
