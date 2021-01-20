package tw.waterball.ddd.waber.springboot.commons.profiles;

/**
 * @author Waterball (johnny850807@gmail.com)
 */


import org.springframework.context.annotation.Profile;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile(FakeServiceDrivers.NAME)
public @interface FakeServiceDrivers {
    String NAME = "fakeServiceDrivers";
}
