package tw.waterball.ddd.model.user;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class DriverIsNotAvailableException extends RuntimeException {
    public DriverIsNotAvailableException(Driver driver) {
        super("The driver " + driver.getName() + " has been matched.");
    }

    public DriverIsNotAvailableException(String message) {
        super(message);
    }

    public DriverIsNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public DriverIsNotAvailableException(Throwable cause) {
        super(cause);
    }

    public DriverIsNotAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
