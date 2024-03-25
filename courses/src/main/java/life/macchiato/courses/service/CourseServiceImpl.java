package life.macchiato.courses.service;

import life.macchiato.courses.dto.CourseRequest;
import life.macchiato.courses.dto.CourseResponse;
import life.macchiato.courses.dto.SearchResponse;
import life.macchiato.courses.exception.ResourceNotFoundException;
import life.macchiato.courses.model.Course;
import life.macchiato.courses.model.Search;
import life.macchiato.courses.model.Torrent;
import life.macchiato.courses.repository.CourseRepository;
import life.macchiato.courses.repository.SearchRepository;
import life.macchiato.courses.util.FCOScraper;
import life.macchiato.courses.util.TransCli;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pro.macchiato.cli.CliResult;

import java.util.List;
import java.util.Optional;

import static life.macchiato.courses.model.Torrent.Status.*;

@Service
@Slf4j
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepo;
    SearchRepository searchRepo;

    @Override
    public List<CourseResponse> coursesFromSearch(Long searchId) throws ResourceNotFoundException {
        Optional<Search> byId = searchRepo.findById(searchId);
        if (byId.isEmpty())
        {
            throw new ResourceNotFoundException(String.format("Search with id '%d' not found", searchId));
        }

        return byId.get().getCourses().stream()
                .map(Course::toResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> allCourses(Torrent.Status status, String name) {
        return courseRepo.findAll(Sort.by(Sort.Direction.DESC, "updatedAt")).parallelStream()
                .filter(course -> {
                    if(status != null) {
                        if (!course.getTorrent().getStatus().equals(status)) return false;
                    }
                    if (name != null) {

                        if (course.getName().toLowerCase().contains(name.toLowerCase().strip())) return true;
                        else return course.getCreator().toLowerCase().contains(name.toLowerCase().strip());
                    }

                    return true;
                })
                .map(Course::toResponse)
                .toList();
    }

    @Override
    public List<SearchResponse> allSearches() {
        return searchRepo.findAll(Sort.by(Sort.Direction.DESC, "updatedAt")).stream()
                .map(Search::toResponse)
                .toList();
    }

    @Override
    public Search searchCourses(CourseRequest courseRequest) {
        log.info("Search for courses: {}", courseRequest);
        Optional<Search> searchByName = searchRepo.findSearchByName(courseRequest.name());
        if (searchByName.isPresent())
        {
            return searchByName.get();
        }

        try  {
            FCOScraper scraper = new FCOScraper();
            List<Course> courses = scraper.findByName(courseRequest.name())
                    .stream()
                    .map((course) -> {
                        Optional<Course> courseByHref = courseRepo.findCourseByHref(course.getHref());
                        return courseByHref.orElse(course);})
                    .toList();


            courseRepo.saveAll(courses);

            Search search = new Search();
            search.setCourses(courses);
            search.setName(courseRequest.name());

            searchRepo.save(search);

            return search;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async("torrentExecutor")
    public void findTorrent(Course... courses) {
        FCOScraper scraper = new FCOScraper();
        for (Course course : courses) {
            Torrent torrent = course.getTorrent();
            if (torrent.getStatus().equals(PENDING))
            {
                if (scraper.findTorrent(course)) {
                    torrent.setStatus(NOT_STARTED);
                } else {
                    torrent.setStatus(NOT_FOUND);
                }
            }
        }

        log.info(courseRepo.saveAll(List.of(courses)).toString());
    }

    @Override
    @Async("torrentExecutor")
    public void findTorrent(Search search) {
        FCOScraper scraper = new FCOScraper();

        search.getCourses().parallelStream()
                .forEach(course -> {
                    Torrent torrent = course.getTorrent();
                    if (torrent.getStatus().equals(PENDING))
                    {
                        if (scraper.findTorrent(course)) {
                            torrent.setStatus(NOT_STARTED);
                        } else {
                            torrent.setStatus(NOT_FOUND);
                        }
                        courseRepo.save(course);
                    }
                });
    }

    @Override
    public Course courseFromId(Long courseId) throws ResourceNotFoundException {
        Optional<Course> byId = courseRepo.findById(courseId);
        if (byId.isEmpty())
        {
            throw new ResourceNotFoundException(String.format("Course with id '%d' not found", courseId));
        }

        return byId.get();
    }

    @Override
    @Async("torrentExecutor")
    public void executeTrans(Course course) {
        Torrent torrent = course.getTorrent();

        switch (torrent.getStatus())
        {
            case NOT_FOUND:
            case COMPLETED :
            case IN_PROGRESS:
                log.info("{}: {}", torrent.getStatus().name(), course.getName());
                return;
            default:
        }

        log.info("execute {}", course.toResponse());
        TransCli trans = new TransCli(torrent.getHref());
        trans.setLabel(course.getName());
        trans.addOption("--no-downlimit");

        torrent.setStatus(IN_PROGRESS);
        courseRepo.save(course);

        try {

            CliResult result = trans.execute();
            log.info("{}", result.elapsed());
            if (result.exitCode() <= 0) {
                torrent.setStatus(COMPLETED);
            } else {
                torrent.setStatus(UNKNOWN);
            }
            courseRepo.save(course);

        }  catch (Exception e) {
            log.error(e.getMessage());
            torrent.setStatus(NOT_STARTED);
            courseRepo.save(course);
            throw new RuntimeException(e);
        }

        log.info("{}: {}", torrent.getStatus().name(), course.getName());
    }
}
