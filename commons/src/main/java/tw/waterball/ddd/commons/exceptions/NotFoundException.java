package tw.waterball.ddd.waber.springboot.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends IllegalArgumentException {
}
