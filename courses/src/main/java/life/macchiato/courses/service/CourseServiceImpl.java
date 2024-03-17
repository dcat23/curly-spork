package life.macchiato.courses.service;

import jakarta.transaction.Transactional;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
    public List<CourseResponse> allCourses() {
        return courseRepo.findAll(Sort.by(Sort.Direction.DESC, "updatedAt")).stream()
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
            if (torrent.getStatus().equals(UNKNOWN))
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
    @Transactional
    @Async("torrentExecutor")
    public void findTorrent(Search search) {
        FCOScraper scraper = new FCOScraper();
        for (Course course : search.getCourses()) {
            Torrent torrent = course.getTorrent();
            if (torrent.getStatus().equals(UNKNOWN))
            {
                if (scraper.findTorrent(course)) {
                    torrent.setStatus(NOT_STARTED);
                } else {
                    torrent.setStatus(NOT_FOUND);
                }
                courseRepo.save(course);
            }
        }
    }
}
