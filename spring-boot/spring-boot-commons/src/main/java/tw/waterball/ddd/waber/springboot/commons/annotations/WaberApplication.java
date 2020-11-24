package tw.waterball.ddd.waber.springboot.commons.annotations;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.annotation.*;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication(scanBasePackages = "tw.waterball.ddd")
public @interface WaberApplication {
}
