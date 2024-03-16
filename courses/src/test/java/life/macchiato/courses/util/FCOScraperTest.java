package life.macchiato.courses.util;

import life.macchiato.courses.model.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;

class FCOScraperTest {

    private FCOScraper scraper;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        scraper = new FCOScraper();
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        scraper = null;
        closeable.close();
    }

    @Test @Disabled
    void findByName() {
    }

    @Test
    void shouldFindTorrent() {
        Course course = Course.builder()
                .href("https://get.freecoursesonline.me/linkedin-full-stack-web-applications-with-rust-and-leptos/")
                .build();

        scraper.findTorrent(course);

        assertThat(course.getTorrent()).isNotNull();
    }
}