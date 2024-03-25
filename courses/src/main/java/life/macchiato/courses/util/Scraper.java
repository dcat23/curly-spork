package life.macchiato.courses.util;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URL;

public class Scraper {
    public static final String baseUrl = "https://get.freecoursesonline.me/";

    protected final UriComponentsBuilder uri;

    public Scraper(UriComponentsBuilder uri) {
        this.uri = uri;
    }

    public Scraper() {
        uri = UriComponentsBuilder.fromHttpUrl(baseUrl);
    }

    public HtmlPage getPage() {
        return getPage(uri.toUriString());
    }

    public static HtmlPage getPage(URL url) {
        return getPage(url.toString());
    }
    public static HtmlPage getPage(String url) {
        HtmlPage page = null;
        try(WebClient client = new WebClientBuilder().build())
        {
            page = client.getPage(url);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return page;
    }
}