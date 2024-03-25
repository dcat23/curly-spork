package life.macchiato.youtube.controller;


import life.macchiato.youtube.dto.CreateSearchDTO;
import life.macchiato.youtube.model.VideoRequest;
import life.macchiato.youtube.service.YoutubeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/youtube")
public class YoutubeController {

    YoutubeService youtubeService;

    public YoutubeController(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.status(HttpStatus.OK).body("hello");
    }


    @GetMapping("/all")
    public ResponseEntity<?> showRequests() {

        try {
            List<VideoRequest> requests = youtubeService.showRequests();
            return ResponseEntity.status(HttpStatus.OK).body(requests);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getLocalizedMessage());
        }
    }
    @PostMapping("/search")
    public ResponseEntity<?> createSearch(@RequestBody CreateSearchDTO searchDTO) {

        try {
            youtubeService.createSearch(searchDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        }
        catch (GeneralSecurityException e) {
            return ResponseEntity.status(405).body(e.getLocalizedMessage());
        }
        catch (IOException e) {
            return ResponseEntity.status(401).body(e.getLocalizedMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(400).body(e.getLocalizedMessage());
        }
    }

}
