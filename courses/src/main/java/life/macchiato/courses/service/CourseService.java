package life.macchiato.courses.service;

import jakarta.transaction.Transactional;
import life.macchiato.courses.dto.CourseRequest;
import life.macchiato.courses.exception.ResourceNotFoundException;
import life.macchiato.courses.model.Course;
import life.macchiato.courses.model.Search;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {
    List<Course> coursesFromSearch(Long searchId) throws ResourceNotFoundException;
    List<Course> allCourses();
    List<Search> allSearches();

    Search searchCourses(CourseRequest courseRequest);

    @Async("torrentExecutor")
    void findTorrent(Course... courses);

    @Async("torrentExecutor")
    void findTorrents(Search search);
}
