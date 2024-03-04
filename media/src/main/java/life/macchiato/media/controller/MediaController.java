package life.macchiato.media.controller;

import com.jfposton.ytdlp.YtDlpException;
import life.macchiato.media.dto.DownloadRequest;
import life.macchiato.media.service.MediaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/media")
@AllArgsConstructor
public class MediaController {

    MediaService mediaService;

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.status(200).body("hello");
    }

    @GetMapping("/status/{fileId}")
    public ResponseEntity<?> status(@PathVariable long fileId) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND).body(mediaService.status(fileId));
        } catch(Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestVideo(@RequestBody DownloadRequest request) {
        try {
            mediaService.requestVideo(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch(YtDlpException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
