package tw.waterball.ddd.waber.springboot.commons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.ddd.api.match.FakeMatchServiceDriver;
import tw.waterball.ddd.api.trip.FakeTripServiceDriver;
import tw.waterball.ddd.waber.api.payment.FakeUserServiceDriver;
import tw.waterball.ddd.waber.springboot.commons.profiles.FakeServiceDrivers;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@FakeServiceDrivers
@Configuration
public class FakeServiceDriverConfiguration {


    @Bean
    public FakeUserServiceDriver fakeUserServiceDriver() {
        return new FakeUserServiceDriver();
    }

    @Bean
    public FakeMatchServiceDriver fakeMatchServiceDriver() {
        return new FakeMatchServiceDriver();
    }

    @Bean
    public FakeTripServiceDriver fakeTripServiceDriver() {
        return new FakeTripServiceDriver();
    }
}
