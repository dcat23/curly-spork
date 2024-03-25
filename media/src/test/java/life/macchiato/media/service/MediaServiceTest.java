package life.macchiato.media.service;

import life.macchiato.media.dto.DownloadStatus;
import life.macchiato.media.dto.MediaRequest;
import life.macchiato.media.exception.ResourceNotFound;
import life.macchiato.media.model.Media;
import life.macchiato.media.repository.MediaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static life.macchiato.media.dto.DownloadStatus.*;
import static org.assertj.core.api.Assertions.*;

class MediaServiceTest {

    @Mock
    private MediaRepository mediaRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @InjectMocks
    private MediaServiceImpl mediaService;

    @Test
    void mediaServiceStatusShouldThrow() {
        assertThatThrownBy(() -> {
            mediaService.status(0);
        }).isInstanceOf(ResourceNotFound.class);
    }


    @Test
    void mediaServiceShouldRequestVideo() {
        String url = "https://www.youtube.com/watch?v=202U6qyVIqY";
        Media actual = null;

        try {
            actual = mediaService.requestVideo(new MediaRequest(url, "m4a"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThat(actual).isNotNull();
        assertThat(actual.getVideoId()).isEqualTo("202U6qyVIqY");

    }
}