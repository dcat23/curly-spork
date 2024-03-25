package life.macchiato.courses.model;

import jakarta.persistence.*;
import life.macchiato.courses.dto.SearchResponse;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "search_id", referencedColumnName = "id")
    private List<Course> courses;

    public SearchResponse toResponse() {
        return new SearchResponse(id, name, courses.size());
    }
}