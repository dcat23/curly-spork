package life.macchiato.courses.model;

import jakarta.persistence.*;
import life.macchiato.courses.dto.CourseResponse;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String creator;
    private String href;
    private String image;

    @OneToOne(cascade = CascadeType.ALL)
    private Torrent torrent;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public CourseResponse toResponse() {
        return new CourseResponse(
                id,
                name,
                creator,
                torrent.getFileSize(),
                torrent.getStatus()
        );
    }
}
