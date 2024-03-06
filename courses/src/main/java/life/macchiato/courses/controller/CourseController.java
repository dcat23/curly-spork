package life.macchiato.courses.controller;


import life.macchiato.courses.dto.CourseRequest;
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

    @GetMapping("/{id}")
    ResponseEntity<?> coursesFromSearch(@PathVariable(name = "id") Long searchId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(courseService.coursesFromSearch(searchId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/")
    ResponseEntity<?> searchCourses(@RequestBody CourseRequest courseRequest) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(courseService.searchCourses(courseRequest));
    }
}
