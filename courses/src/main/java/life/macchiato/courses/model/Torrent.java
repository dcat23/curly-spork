package life.macchiato.courses.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Torrent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String href;
    private String fileSize;
    private String source;
    @Enumerated
    private Status status;

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
