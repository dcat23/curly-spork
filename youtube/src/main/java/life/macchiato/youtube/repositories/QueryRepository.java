package life.macchiato.youtube.repositories;

import life.macchiato.youtube.models.SearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QueryRepository extends JpaRepository<SearchQuery, Long> {
    Optional<SearchQuery> findByValueAndPageToken(@Param("value") String value, @Param("pageToken") String pageToken);

}