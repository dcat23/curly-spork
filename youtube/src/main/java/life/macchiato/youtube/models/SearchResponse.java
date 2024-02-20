package life.macchiato.youtube.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Responses")
@Table(name = "responses")
public class SearchResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    private String etag;
    private String query;
    private String pageToken;
    private String nextPageToken;
    private String previousPageToken;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_resp_id", referencedColumnName = "id")
    private List<VideoResult> results;
}
