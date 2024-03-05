package life.macchiato.media.service;

import com.jfposton.ytdlp.*;
import com.jfposton.ytdlp.mapper.VideoInfo;
import life.macchiato.media.dto.MediaRequest;
import life.macchiato.media.dto.DownloadStatus;
import life.macchiato.media.exception.ResourceNotFound;
import life.macchiato.media.model.Media;
import life.macchiato.media.repository.MediaRepository;
import life.macchiato.media.util.Downloader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static life.macchiato.media.dto.DownloadStatus.*;

@Slf4j
@Service
@AllArgsConstructor
public class MediaServiceImpl implements MediaService {

    MediaRepository mediaRepository;

    @Override
    public DownloadStatus status(long id) throws ResourceNotFound {
        Optional<Media> byId = mediaRepository.findById(id);
        if (byId.isEmpty()) {
            throw new ResourceNotFound("Resource with id {} not found");
        }
        return byId.get().getStatus();
    }

    @Override
    @Async("downloadExecutor")
    public CompletableFuture<Media> requestVideo(MediaRequest request) throws Exception {
        log.info("Looking up media {}", request);

        URL url =  new URL(request.url());
        Optional<Media> byOriginUrl = mediaRepository.findByUrl(url.toString());

        Media media;
        if (byOriginUrl.isPresent())
        {
            log.info("found");
            media = byOriginUrl.get();
        }
        else {
            log.info("fetching video info");
            VideoInfo v = YtDlp.getVideoInfo(url.toString());

            String title = StringUtils.getFilename(v.getTitle());

            media = Media.builder()
                    .name(title)
                    .url(url.toString())
                    .ext(request.format())
                    .videoId(v.getId())
                    .status(NOT_STARTED)
                    .build();
        }

        switch (media.getStatus())
        {
            case COMPLETE:
            case IN_PROGRESS:
                log.info("status: {}", media.getStatus());
                return CompletableFuture.completedFuture(media);
        }

        mediaRepository.save(media);

        log.info("saved {}", media);

        try {
            media.setStatus(IN_PROGRESS);
            mediaRepository.flush();
            Downloader download = new Downloader.builder(media.getUrl())
                .format(media.getExt())
                .directory("tmp",
                        media.getExt(),
                        media.getVideoId())
                .build();

            download.execute();
            media.setStatus(COMPLETE);

        } catch (Exception e) {
            media.setStatus(IN_PROGRESS);
            e.printStackTrace();
        }


        mediaRepository.saveAndFlush(media);
        return CompletableFuture.completedFuture(media);
    }

    @Async("downloadExecutor")
    public void download(Media media) throws Exception {
//        log.info("Download executor");

//        Downloader download = new Downloader.builder(media.getOriginUrl())
//                .directory(
//                        "download",
//                        media.getExt(),
//                        String.valueOf(media.getId()))
//                .format(media.getExt())
//                .build();
//
//        YtDlpRequest downloadRequest = new YtDlpRequest(media.getOriginUrl(), outputPath);
//        downloadRequest.setOption("format", media.getExt());
//
//        log.info("Starting download {}", downloadRequest.getDirectory());
//
//        YtDlpResponse response = YtDlp.execute(downloadRequest, null, (v, l) -> log.info("progress {} - {}", v, media.getName()));
//
//
//        log.info(response.getOut());
    }
}
