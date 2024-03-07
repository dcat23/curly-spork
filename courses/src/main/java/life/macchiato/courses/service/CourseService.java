package life.macchiato.courses.service;

import life.macchiato.courses.dto.CourseRequest;
import life.macchiato.courses.exception.ResourceNotFoundException;
import life.macchiato.courses.model.Course;
import life.macchiato.courses.model.Search;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface CourseService {
    Set<Course> coursesFromSearch(Long searchId) throws ResourceNotFoundException;

    Search searchCourses(CourseRequest courseRequest);
}
