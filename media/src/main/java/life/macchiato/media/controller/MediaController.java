package life.macchiato.media.controller;

import com.jfposton.ytdlp.YtDlpException;
import life.macchiato.media.dto.MediaRequest;
import life.macchiato.media.exception.ResourceNotFound;
import life.macchiato.media.service.MediaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

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
    public ResponseEntity<?> status(@PathVariable long fileId) throws ResourceNotFound {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mediaService.status(fileId));
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestVideo(@RequestBody MediaRequest mediaRequest) throws Exception {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mediaService.requestVideo(mediaRequest));
    }
}
