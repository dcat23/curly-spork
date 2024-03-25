package life.macchiato.courses.util;

import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import static life.macchiato.courses.util.Scraper.baseUrl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ScraperTest {

    UriComponentsBuilder uri;
    Scraper scraper;
    @BeforeEach
    void setUp() {
        uri = UriComponentsBuilder.fromHttpUrl(baseUrl);
        scraper = new FCOScraper();
    }

    @AfterEach
    void tearDown() {
        scraper = null;
    }

    @Test
    void shouldFindPageTitle() {
        HtmlPage page = scraper.getPage();
        String expected = "Free Courses Online With Certificates (2024) | Download Courses Torrents Videos Free | [FCO] FreeCoursesOnline.Me";
        assertThat(page.getTitleText()).isEqualTo(expected);
    }
}