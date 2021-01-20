package tw.waterball.ddd.waber.springboot.commons.profiles;

/**
 * @author Waterball (johnny850807@gmail.com)
 */


import org.springframework.context.annotation.Profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile(Microservice.NAME)
public @interface Microservice {
    String NAME = "microservice";
}
