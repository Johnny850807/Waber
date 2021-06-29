package tw.waterball.ddd.waber.springboot.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.api.match.RestMatchContext;
import tw.waterball.ddd.api.trip.RestTripContext;
import tw.waterball.ddd.waber.api.payment.RestPaymentContext;
import tw.waterball.ddd.waber.api.payment.RestUserContext;
import tw.waterball.ddd.waber.springboot.commons.profiles.Microservice;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Microservice
@Configuration
public class MicroserviceConfiguration {

    @Bean
    public RestUserContext restUserServiceDriver(WaberProperties waberProperties,
                                                 RestTemplate restTemplate) {
        return new RestUserContext(waberProperties.getClient().getUserService(), restTemplate);
    }

    @Bean
    public RestTripContext restTripServiceDriver(WaberProperties waberProperties,
                                                 RestTemplate restTemplate) {
        return new RestTripContext(waberProperties.getClient().getTripService(), restTemplate);
    }

    @Bean
    public RestMatchContext restMatchServiceDriver(WaberProperties waberProperties,
                                                   RestTemplate restTemplate) {
        return new RestMatchContext(waberProperties.getClient().getMatchService(), restTemplate);
    }

    @Bean
    public RestPaymentContext restPaymentServiceDriver(WaberProperties waberProperties,
                                                       RestTemplate restTemplate) {
        return new RestPaymentContext(waberProperties.getClient().getPaymentService(), restTemplate);
    }

    @Bean
    public SmartInitializingSingleton log(WaberProperties waberProperties) {
        return () -> {
            try {
                var mapper = new ObjectMapper();

                log.info("Waber properties: {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(waberProperties));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        };
    }

}
