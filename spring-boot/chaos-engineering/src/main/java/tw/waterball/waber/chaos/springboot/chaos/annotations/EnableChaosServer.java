package tw.waterball.waber.chaos.springboot.chaos.annotations;

import org.springframework.context.annotation.Import;
import tw.waterball.waber.chaos.springboot.chaos.config.ChaosEngineeringConfiguration;
import tw.waterball.waber.chaos.springboot.chaos.config.TcpServerConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({TcpServerConfiguration.class})
public @interface EnableChaosServer {
}
