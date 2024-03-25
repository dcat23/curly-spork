package life.macchiato.courses.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.MalformedURLException;
import java.time.LocalDateTime;

@ControllerAdvice
public class DefaultExceptionHandler {


    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<ApiError> runtimeExceptionHandler(
            MalformedURLException  e,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                request.getRequestId(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ApiError> runtimeExceptionHandler(
            HttpServerErrorException  e,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                request.getRequestId(),
                e.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> runtimeExceptionHandler(
            HttpMessageNotReadableException e,
            HttpServletRequest request
    ) {

        ApiError apiError = new ApiError(
                request.getRequestId(),
                "Incorrect format. Choices: [AUDIO, VIDEO]",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> notFoundExceptionHandler(
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> mismatchExceptionHandler(
            MethodArgumentTypeMismatchException e,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "Incorrect status. Choices: " +
                        "NOT_STARTED, " +
                        "IN_PROGRESS, " +
                        "COMPLETE, " +
                        "UNKNOWN",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }


}
