package tw.waterball.ddd.waber.springboot.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.api.user.RestUserServiceDriver;
import tw.waterball.ddd.api.user.UserServiceDriver;
import tw.waterball.ddd.waber.springboot.commons.profiles.Microservice;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class ServiceDriverConfiguration {

    @Bean
    @Microservice
    public UserServiceDriver userServiceDriver(WaberProperties waberProperties,
                                               RestTemplate restTemplate) {
        return new RestUserServiceDriver(waberProperties.getClient().getUserService(), restTemplate);
    }
}
