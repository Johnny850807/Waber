package tw.waterball.ddd.waber.springboot.payment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tw.waterball.ddd.model.payment.CompositePricingStrategy;
import tw.waterball.ddd.model.payment.PricingStrategy;

import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class PricingStrategyConfiguration {

    @Bean
    @Primary
    public CompositePricingStrategy compositePricingStrategy(List<PricingStrategy> pricingStrategies) {
        return new CompositePricingStrategy(pricingStrategies);
    }
}
