package tw.waterball.ddd.waber.springboot.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.RestMatchServiceDriver;
import tw.waterball.ddd.api.user.RestUserServiceDriver;
import tw.waterball.ddd.api.user.UserServiceDriver;
import tw.waterball.ddd.waber.springboot.commons.profiles.Microservice;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Microservice
@Configuration
public class ServiceDriverConfiguration {

    @Bean
    public UserServiceDriver userServiceDriver(ObjectMapper objectMapper,
                                               WaberProperties waberProperties,
                                               RestTemplate restTemplate) {
        return new RestUserServiceDriver(objectMapper, waberProperties.getClient().getUserService(), restTemplate);
    }


    @Bean
    public MatchServiceDriver matchServiceDriver(WaberProperties waberProperties,
                                                RestTemplate restTemplate) {
        return new RestMatchServiceDriver(waberProperties.getClient().getMatchService(), restTemplate);
    }
}
