package tw.waterball.ddd.waber.springboot.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.api.match.RestMatchServiceDriver;
import tw.waterball.ddd.api.trip.RestTripServiceDriver;
import tw.waterball.ddd.waber.api.payment.RestPaymentServiceDriver;
import tw.waterball.ddd.waber.api.payment.RestUserServiceDriver;
import tw.waterball.ddd.waber.springboot.commons.profiles.Microservice;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Microservice
@Configuration
public class MicroservicesConfiguration {

    @Bean
    public RestUserServiceDriver restUserServiceDriver(ObjectMapper objectMapper,
                                                       WaberProperties waberProperties,
                                                       RestTemplate restTemplate) {
        return new RestUserServiceDriver(objectMapper, waberProperties.getClient().getUserService(), restTemplate);
    }

    @Bean
    public RestTripServiceDriver restTripServiceDriver(WaberProperties waberProperties,
                                                       RestTemplate restTemplate) {
        return new RestTripServiceDriver(waberProperties.getClient().getTripService(), restTemplate);
    }

    @Bean
    public RestMatchServiceDriver restMatchServiceDriver(WaberProperties waberProperties,
                                                         RestTemplate restTemplate) {
        return new RestMatchServiceDriver(waberProperties.getClient().getMatchService(), restTemplate);
    }

    @Bean
    public RestPaymentServiceDriver restPaymentServiceDriver(WaberProperties waberProperties,
                                                             RestTemplate restTemplate) {
        return new RestPaymentServiceDriver(waberProperties.getClient().getPaymentService(), restTemplate);
    }

}
