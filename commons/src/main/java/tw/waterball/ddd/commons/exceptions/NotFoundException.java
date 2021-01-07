package tw.waterball.ddd.commons.exceptions;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class NotFoundException extends IllegalArgumentException {
    public NotFoundException() {
    }

    public NotFoundException(String s) {
        super(s);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
