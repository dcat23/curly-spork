package life.macchiato.courses.service;

import life.macchiato.courses.dto.CourseRequest;
import life.macchiato.courses.model.Course;
import life.macchiato.courses.model.Search;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {
    List<Course> coursesFromSearch(Long searchId);

    Search searchCourses(CourseRequest courseRequest);
}
