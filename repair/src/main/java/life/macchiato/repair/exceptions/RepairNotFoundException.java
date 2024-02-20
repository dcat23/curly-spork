package life.macchiato.repair.exceptions;

public class RepairNotFoundException extends Throwable {
    public RepairNotFoundException() {
    }

    public RepairNotFoundException(String message) {
        super(message);
    }

    public RepairNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepairNotFoundException(Throwable cause) {
        super(cause);
    }

    public RepairNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
