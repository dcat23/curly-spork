package life.macchiato.youtube.controller;


import life.macchiato.youtube.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@RestController
@RequestMapping("/api/v1/youtube")
public class MediaController {

    MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.status(200).body("hello");
    }

    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<?> youtubeInfo(@RequestParam(name = "title") String videoTitle) {

        try {
            return ResponseEntity.status(201).body(mediaService.allYoutubeItems(videoTitle.strip()));
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
