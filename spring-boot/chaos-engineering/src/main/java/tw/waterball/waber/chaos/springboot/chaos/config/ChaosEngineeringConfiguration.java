package tw.waterball.waber.chaos.springboot.chaos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.waber.chaos.core.WaberChaos;
import tw.waterball.waber.chaos.core.WaberChaosEngine;

import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class ChaosEngineeringConfiguration {

    @Bean
    public WaberChaosEngine waberChaosEngine(List<WaberChaos> waberChaos) {
        return new WaberChaosEngine(waberChaos);
    }


}
