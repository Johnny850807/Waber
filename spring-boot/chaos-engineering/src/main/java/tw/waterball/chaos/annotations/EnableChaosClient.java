package tw.waterball.chaos.annotations;

import org.springframework.context.annotation.Import;
import tw.waterball.chaos.config.TcpClientConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({TcpClientConfiguration.class})
public @interface EnableChaosClient {
}
