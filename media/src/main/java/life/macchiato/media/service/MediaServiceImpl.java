package life.macchiato.media.service;

import com.jfposton.ytdlp.*;
import com.jfposton.ytdlp.mapper.VideoInfo;
import life.macchiato.media.dto.MediaRequest;
import life.macchiato.media.dto.DownloadStatus;
import life.macchiato.media.model.Media;
import life.macchiato.media.repository.MediaRepository;
import life.macchiato.media.util.Downloader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class MediaServiceImpl implements MediaService {

    MediaRepository mediaRepository;

    @Override
    @Async
    public CompletableFuture<DownloadStatus> status(long id) {
        return CompletableFuture.completedFuture(new DownloadStatus("none"));
    }

    @Override
    @Async("downloadExecutor")
    public CompletableFuture<Media> requestVideo(MediaRequest request) throws Exception {
        log.info("Looking up media {}", request);
        URL originUrl =  new URL(request.url());

        Optional<Media> byOriginUrl = mediaRepository.findByOriginUrl(originUrl.toString());

        Media media;
        if (byOriginUrl.isPresent())
        {
            log.info("found");
            media = byOriginUrl.get();
        }
        else {
            log.info("fetching video info");
            VideoInfo v = YtDlp.getVideoInfo(request.url());

            String ext = request.format();
            String title = StringUtils.getFilename(v.getTitle());

            media = Media.builder()
                    .name(title)
                    .originUrl(originUrl.toString())
                    .videoId(v.getId())
                    .ext(ext)
                    .build();
        }


        media.setThread(Thread.currentThread().getName());
        mediaRepository.save(media);

        log.info("saved {}", media);

        try {
            Downloader download = new Downloader.builder(media.getOriginUrl())
                .format(media.getExt())
                .directory("download",
                        media.getExt(),
                        String.valueOf(media.getId()))
                .build();

            download.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


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
