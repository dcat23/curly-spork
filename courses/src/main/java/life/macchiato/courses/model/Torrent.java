package life.macchiato.courses.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Torrent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String href;
    private String fileSize;
    @Enumerated
    private Status status;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonIgnore
    private Course course;

    public Torrent() {
        status = Status.UNKNOWN;
    }

    public enum Status {
        UNKNOWN,
        NOT_STARTED,
        NOT_FOUND,
        IN_PROGRESS,
        COMPLETED
    }
}
