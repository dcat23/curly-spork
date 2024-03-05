package life.macchiato.media.service;

import com.jfposton.ytdlp.*;
import com.jfposton.ytdlp.mapper.VideoInfo;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static life.macchiato.media.dto.DownloadStatus.*;

@Slf4j
@Service
@AllArgsConstructor
public class MediaServiceImpl implements MediaService {

    MediaRepository mediaRepository;

    @Override
    public Media status(long id) throws ResourceNotFound {
        Optional<Media> byId = mediaRepository.findById(id);
        if (byId.isEmpty()) {
            throw new ResourceNotFound(String.format("Resource with id %d not found", id));
        }
        return byId.get();
    }

//    @Override
//    public List<Media> allByStatus(@Nullable DownloadStatus status) {
//        List<Media> allMedia;
//        if (status == null)
//        {
//            allMedia = mediaRepository.findAll();
//        } else {
//            allMedia = mediaRepository.findAllByStatus(status);
//        }
//
//        return allMedia;
//    }


    @Override
    public Media requestVideo(MediaRequest request) throws Exception {
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

            VideoInfo v;
            try {
                v = YtDlp.getVideoInfo(url.toString());
            } catch (YtDlpException e) {
                throw new RuntimeException(e);
            }


            String title = StringUtils.getFilename(v.getTitle());

            media = Media.builder()
                    .name(title)
                    .url(url.toString())
                    .ext(request.format())
                    .videoId(v.getId())
                    .status(NOT_STARTED)
                    .build();
        }

        mediaRepository.save(media);
        return media;
    }

    @Override
    @Transactional
    @Async("downloadExecutor")
    public void execute(Media media)  {

        switch (media.getStatus())
        {
            case COMPLETE:
            case IN_PROGRESS:
                log.info("status: {}", media.getStatus());
                return;
        }

        media.setStatus(IN_PROGRESS);
        mediaRepository.save(media);

        Downloader download = new Downloader.builder(media.getUrl())
                .directory("tmp", media.getVideoId())
                .format(media.getExt())
                .build();

        download.execute();
        media.setStatus(COMPLETE);
        mediaRepository.save(media);
    }

}
