package life.macchiato.courses.controller;

import life.macchiato.courses.dto.CourseRequest;
import life.macchiato.courses.exception.ResourceNotFoundException;
import life.macchiato.courses.model.Course;
import life.macchiato.courses.model.Search;
import life.macchiato.courses.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/courses")
@AllArgsConstructor
public class CourseController {

    CourseService courseService;

    @GetMapping
    ResponseEntity<?> allCourses() {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(courseService.allCourses());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> courseFromId(@PathVariable(name = "id") Long courseId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.courseFromId(courseId));
    }

    @GetMapping("/search/{id}")
    ResponseEntity<?> coursesFromSearch(@PathVariable(name = "id") Long searchId) throws ResourceNotFoundException {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(courseService.coursesFromSearch(searchId));
    }

    @PostMapping("/search")
    ResponseEntity<?> searchCourses(@RequestBody CourseRequest courseRequest) {
        Search search = courseService.searchCourses(courseRequest);
        courseService.findTorrent(search);
        return ResponseEntity.status(HttpStatus.CREATED).body(search.toResponse());
    }

    @GetMapping("/search")
    ResponseEntity<?> allSearches() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.allSearches());
    }

    @PostMapping("/execute/{id}")
    ResponseEntity<?> execute(@PathVariable(name = "id") Long courseId) throws ResourceNotFoundException {
        Course course = courseService.courseFromId(courseId);
        courseService.executeTrans(course);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Started " + course.getName());
    }
}
