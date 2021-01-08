package tw.waterball.ddd.model.user;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class DriverHasBeenMatchedException extends RuntimeException {
    public DriverHasBeenMatchedException() {
    }

    public DriverHasBeenMatchedException(String message) {
        super(message);
    }

    public DriverHasBeenMatchedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DriverHasBeenMatchedException(Throwable cause) {
        super(cause);
    }

    public DriverHasBeenMatchedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
