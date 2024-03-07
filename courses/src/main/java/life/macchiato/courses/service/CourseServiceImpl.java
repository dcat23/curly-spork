package life.macchiato.courses.service;

import life.macchiato.courses.dto.CourseRequest;
import life.macchiato.courses.exception.ResourceNotFoundException;
import life.macchiato.courses.model.Course;
import life.macchiato.courses.model.Search;
import life.macchiato.courses.repository.CourseRepository;
import life.macchiato.courses.repository.SearchRepository;
import life.macchiato.courses.util.FCOScraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepo;
    SearchRepository searchRepo;

    @Override
    public List<Course> coursesFromSearch(Long searchId) throws ResourceNotFoundException {
        Optional<Search> byId = searchRepo.findById(searchId);
        if (byId.isEmpty())
        {
            throw new ResourceNotFoundException(String.format("Resource with id '%d' not found", searchId));
        }

        return byId.get().getCourses();
    }

    @Override
    public List<Course> allCourses() {
        return courseRepo.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
    }

    @Override
    public Search searchCourses(CourseRequest courseRequest) {
        Optional<Search> searchByName = searchRepo.findSearchByName(courseRequest.name());
        if (searchByName.isPresent())
        {
            return searchByName.get();
        }

        try {
            FCOScraper scraper = new FCOScraper();
            List<Course> courses = scraper.findByName(courseRequest.name()).stream()
                    .map((course) -> {
                        Optional<Course> courseByHref = courseRepo.findCourseByHref(course.getHref());
                        return courseByHref.orElse(course);
                    }).toList();

            courseRepo.saveAll(courses);
            Search search = Search.builder()
                    .name(courseRequest.name())
                    .courses(courses)
                    .build();
            searchRepo.save(search);
            return search;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
