package tw.waterball.ddd.waber.springboot.commons.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import tw.waterball.chaos.api.ChaosTriggeredException;
import tw.waterball.ddd.commons.exceptions.NotFoundException;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@ControllerAdvice
public class CommonsControllerAdvice {

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @ExceptionHandler({ChaosTriggeredException.class})
    public void handleChaosTriggeredException() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public void handleIllegal() {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public void handleNotFoundException() {
    }
}
