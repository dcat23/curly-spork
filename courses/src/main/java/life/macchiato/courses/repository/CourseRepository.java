package life.macchiato.courses.repository;

import life.macchiato.courses.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findCourseByHref(String href);
}
