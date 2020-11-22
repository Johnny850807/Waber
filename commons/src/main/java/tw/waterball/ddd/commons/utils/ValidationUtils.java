package tw.waterball.ddd.commons.utils;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class ValidationUtils {
    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public static boolean isValid(Object object) {
        return factory.getValidator().validate(object).isEmpty();
    }
}
