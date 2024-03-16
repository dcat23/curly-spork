package life.macchiato.courses.util;

import life.macchiato.courses.model.Course;
import life.macchiato.courses.model.Torrent;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlStrong;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class FCOScraperTest {

    static final String webpage = "https://get.freecoursesonline.me/linkedin-full-stack-web-applications-with-rust-and-leptos/";
    static HtmlPage page;
    @BeforeAll
    static void beforeAll() {
//        page = getPage(webpage);
    }

    private FCOScraper scraper;

    @BeforeEach
    void setUp() {
        scraper = new FCOScraper();
    }

    @AfterEach
    void tearDown() {
        scraper = null;
    }

    @Test
    void shouldFindTorrentByCourse() {
        Course course = Course.builder()
                .torrent(new Torrent())
                .href(webpage)
                .build();

        assertThat(scraper.findTorrent(course)).isTrue();
        Torrent torrent = course.getTorrent();

        assertThat(torrent).isNotNull();
        assertThat(torrent.getHref()).contains(".torrent");
        assertThat(torrent.getFileSize()).isEqualToIgnoringCase("169MB");
    }

    @Test @Disabled
    void shouldFindTorrent() {
        Optional<HtmlAnchor> torrentAnchor = page.getAnchors().stream()
                .filter(a -> a.getHrefAttribute().contains("torrent"))
                .findFirst();

        assertThat(torrentAnchor.isEmpty()).isFalse();
    }

    @Test @Disabled
    void shouldFindFileSize() {
        List<HtmlStrong> byXPath = page.getByXPath("//div//p/strong[contains(text(), \"Size:\")]");
        assertThat(byXPath.isEmpty()).isFalse();

        String size = byXPath.get(0).getTextContent().replace("Size: ", "").strip();
        assertThat(size).isEqualToIgnoringCase("169MB");

    }
}