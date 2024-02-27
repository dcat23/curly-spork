package life.macchiato.youtube.controller;

import life.macchiato.youtube.models.SearchQuery;
import life.macchiato.youtube.models.SearchResponse;
import life.macchiato.youtube.services.YoutubeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/yt")
@AllArgsConstructor
public class YoutubeController {

    @Autowired
    private final YoutubeService youtubeService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello ()
    {
        return ResponseEntity.status(200).body("What's up there?");
    }

    @PostMapping("/info")
    public ResponseEntity<SearchResponse> searchList(@RequestBody SearchQuery request)
    {
        log.info("New info request {}", request);
        return ResponseEntity.status(201)
                .body(youtubeService.searchList(request));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test ()
    {
        return ResponseEntity.status(200).body(youtubeService.testTokens());
    }

    @GetMapping("/video")
    public ResponseEntity<String> findByTitle (@PathVariable String title)
    {
        return ResponseEntity.status(200).body(youtubeService.findByTitle(title));
    }
}
