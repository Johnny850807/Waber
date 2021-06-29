package tw.waterball.ddd.model.user;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class DriverNotAvailableException extends RuntimeException {
    public DriverNotAvailableException(Driver driver) {
        super("The driver " + driver.getName() + " has been matched.");
    }

    public DriverNotAvailableException(String message) {
        super(message);
    }

    public DriverNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public DriverNotAvailableException(Throwable cause) {
        super(cause);
    }

    public DriverNotAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
