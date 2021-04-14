package tw.waterball.chaos.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class ChaosTriggeredException extends RuntimeException {
    public ChaosTriggeredException() {
        super("This is a Chaos!");
    }
}
