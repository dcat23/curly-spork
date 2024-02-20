package life.macchiato.youtube.repositories;

import life.macchiato.youtube.models.SearchResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SearchRepository extends JpaRepository<SearchResponse, Long> {
    Optional<SearchResponse> findByQueryAndPageToken(@Param("query") String query, @Param("pageToken")  String pageToken);
}
