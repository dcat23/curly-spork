package life.macchiato.courses.exception;

public class ResourceNotFoundException extends Throwable {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
