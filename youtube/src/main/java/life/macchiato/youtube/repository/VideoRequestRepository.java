package life.macchiato.youtube.repository;

import life.macchiato.youtube.model.VideoRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoRequestRepository extends JpaRepository<VideoRequest, Long> {
    Optional<VideoRequest> findByName(String name);
}
