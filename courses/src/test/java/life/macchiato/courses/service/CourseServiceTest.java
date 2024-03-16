package life.macchiato.courses.service;

import life.macchiato.courses.dto.CourseRequest;
import life.macchiato.courses.model.Course;
import life.macchiato.courses.model.Search;
import life.macchiato.courses.repository.CourseRepository;
import life.macchiato.courses.repository.SearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static life.macchiato.courses.model.Torrent.Status.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private SearchRepository searchRepository;

    private AutoCloseable closeable;
    private CourseRequest request = new CourseRequest("rust");;

    @InjectMocks
    private CourseServiceImpl service;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        request = null;
        closeable.close();
    }

    @Test @Disabled
    void searchCoursesShouldWork() {
        Search response = service.searchCourses(request);

        assertThat(response.getName()).isEqualTo(request.name());
        assertThat(response.getCourses().isEmpty()).isFalse();

    }
    @Test
    void shouldFindTorrents() {
        Search search = service.searchCourses(request);

        service.findTorrent(search.getCourses().toArray(Course[]::new));

        long torrentsFound = search.getCourses().stream()
                .filter(c -> c.getTorrent().getStatus().equals(NOT_STARTED))
                .count();

        assertThat(torrentsFound).isGreaterThan(0);
    }


}