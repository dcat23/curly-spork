package life.macchiato.courses.model;

import jakarta.persistence.*;
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

    @OneToOne(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Torrent torrent;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
