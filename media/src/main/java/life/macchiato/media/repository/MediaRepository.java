package life.macchiato.media.repository;

import life.macchiato.media.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {

    Optional<Media> findByOriginUrl(String originUrl);
}
