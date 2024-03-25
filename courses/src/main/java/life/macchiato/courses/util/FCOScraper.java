package life.macchiato.courses.util;

import life.macchiato.courses.model.Course;
import life.macchiato.courses.model.Course.CourseBuilder;
import life.macchiato.courses.model.Torrent;
import org.htmlunit.html.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FCOScraper extends Scraper {

    private static final String articlesXpath = "//article[contains(@id, 'post')]";
    private static final String articleTitleSelector = ".entry-title a[rel=\"bookmark\"]";
    private static final Pattern creatorPattern = Pattern.compile("^\\[(.*?)] (.*?)$");
    private static final String torrentSizeXpath = "//div//p/strong[contains(text(), \"Size:\")]";
    private static final String sourceUrlXpath = "//div//p/u[contains(text(), \"Course Source\")]";
    private static final Pattern torrentPattern = Pattern.compile("^.*(?<url>http.*\\.torrent).*$");
    private static final String GROUP_TORRENT_URL = "url";

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
        CourseBuilder builder = Course.builder()
                .torrent(new Torrent())
                .image(image)
                .href(href)
                .name(title)
                .creator("");

        if (m.find())
        {
            String creator = m.group(1).trim();
            String name = m.group(2).trim();
            builder.name(name).creator(creator);
        }
        return builder.build();
    }

    public boolean findTorrent(Course course) {
        HtmlPage page = getPage(course.getHref());
        Optional<HtmlAnchor> torrentAnchor = page.getAnchors().stream()
                .filter(a -> a.getHrefAttribute().contains("torrent"))
                .findFirst();

        if (torrentAnchor.isEmpty()) return false;

        Torrent torrent = course.getTorrent();
        String href = torrentAnchor.get().getHrefAttribute();
        Matcher m = torrentPattern.matcher(href);
        if (!m.matches()) return false;

        torrent.setHref(m.group(GROUP_TORRENT_URL));

        try {
            String size = ((HtmlStrong) page.getByXPath(torrentSizeXpath).get(0))
                    .getTextContent()
                    .replace("Size: ", "")
                    .strip();
            torrent.setFileSize(size);
        } catch (IndexOutOfBoundsException e) {
            torrent.setFileSize("");
        }

        try
        {
            String sourceUrl = ((HtmlUnderlined) page.getByXPath(sourceUrlXpath).get(0))
                    .getNextSibling()
                    .getTextContent()
                    .replaceAll(":\\s*(https?://\\S+)", "$1");
            torrent.setSource(sourceUrl);

        } catch (IndexOutOfBoundsException e) {
            torrent.setSource("");
        }

        return true;
    }
}
