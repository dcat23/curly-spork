package life.macchiato.youtube.repository;

import life.macchiato.youtube.model.YoutubeItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface YoutubeItemRepository extends JpaRepository<YoutubeItem, Long> {

    Optional<YoutubeItem> findByItemId(String itemId);
}
