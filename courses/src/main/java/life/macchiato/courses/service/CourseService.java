package life.macchiato.courses.service;

import life.macchiato.courses.dto.CourseRequest;
import life.macchiato.courses.dto.CourseResponse;
import life.macchiato.courses.dto.SearchResponse;
import life.macchiato.courses.exception.ResourceNotFoundException;
import life.macchiato.courses.model.Course;
import life.macchiato.courses.model.Search;
import life.macchiato.courses.model.Torrent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {
    List<CourseResponse> coursesFromSearch(Long searchId) throws ResourceNotFoundException;
    List<CourseResponse> allCourses(Torrent.Status status, String name);
    List<SearchResponse> allSearches();

    Search searchCourses(CourseRequest courseRequest);

    @Async("torrentExecutor")
    void findTorrent(Course... courses);

    @Async("torrentExecutor")
    void findTorrent(Search search);

    Course courseFromId(Long courseId) throws ResourceNotFoundException;

    @Async("torrentExecutor")
    void executeTrans(Course course) throws ResourceNotFoundException;
}
