package life.macchiato.courses.util;

import life.macchiato.courses.model.Course;
import life.macchiato.courses.model.Torrent;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FCOScraper extends Scraper {

    private static final String articlesXpath = "//article[contains(@id, 'post')]";
    private static final String articleTitleSelector = ".entry-title a[rel=\"bookmark\"]";
    private static final Pattern creatorPattern = Pattern.compile("^\\[(.*?)\\] (.*?)$");


    public FCOScraper() {
        super();
    }

    public Set<Course> findByName(String name) {
        Set<Course> courses = new HashSet<>();
        this.uri.queryParam("s", name);
        List<DomElement> articles = getPage().getByXPath(articlesXpath);
        for (DomElement el : articles)
        {
            String image = el.getElementsByTagName("img").get(0)
                    .getAttribute("data-src");
            String href = el.getElementsByTagName("a").get(0)
                    .getAttribute("href");
            String title = el.querySelector(articleTitleSelector)
                    .getTextContent();
            courses.add(asCourse(image, href, title));
        }

        return courses;
    }


    public static Course asCourse(String image, String href, String title) {

        Matcher m = creatorPattern.matcher(title);
        if (m.find())
        {
            String creator = m.group(1).trim();
            String name = m.group(2).trim();
            return Course.builder()
                    .href(href)
                    .name(name)
                    .image(image)
                    .creator(creator)
                    .build();
        }
        return Course.builder()
                .href(href)
                .name(title)
                .image(image)
                .creator("")
                .build();
    }

    public Optional<Torrent> findTorrent(Course course) {
        HtmlPage page = getPage(course.getHref());
        Optional<HtmlAnchor> torrentAnchor = page.getAnchors().stream()
                .filter(a -> a.getHrefAttribute().contains("torrent"))
                .findFirst();

        if (torrentAnchor.isPresent()) {
            HtmlAnchor anchor = torrentAnchor.get();
            course.setTorrent(anchor.getHrefAttribute());
        }

        return Optional.empty();
    }
}
