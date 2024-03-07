package life.macchiato.courses.repository;

import life.macchiato.courses.model.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long> {
    Optional<Search> findSearchByName(String name);
}
