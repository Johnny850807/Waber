package tw.waterball.ddd.waber.springboot.commons;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.model.geo.LinearDistanceCalculator;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class DistanceCalculatorConfiguration {

    @ConditionalOnMissingBean(DistanceCalculator.class)
    @Bean
    public LinearDistanceCalculator googleDistanceCalculator() {
        return new LinearDistanceCalculator();
    }
}
