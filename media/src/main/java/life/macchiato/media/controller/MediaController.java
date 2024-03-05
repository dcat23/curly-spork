package life.macchiato.media.controller;

import life.macchiato.media.dto.DownloadStatus;
import life.macchiato.media.dto.MediaRequest;
import life.macchiato.media.exception.ResourceNotFound;
import life.macchiato.media.model.Media;
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

//    @GetMapping("/all")
//    public ResponseEntity<?> statusAll(@RequestParam(required = false) DownloadStatus status) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(mediaService.allByStatus(status));
//    }
    @GetMapping("/status/{id}")
    public ResponseEntity<?> status(@PathVariable(name = "id") long mediaId) throws ResourceNotFound {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mediaService.status(mediaId));
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestVideo(@RequestBody MediaRequest mediaRequest) throws Exception {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mediaService.requestVideo(mediaRequest));
    }

    @PostMapping("/execute")
    public ResponseEntity<?> execute(@RequestBody MediaRequest mediaRequest) throws Exception {
        Media media = mediaService.requestVideo(mediaRequest);
        mediaService.execute(media);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(media);
    }
}
