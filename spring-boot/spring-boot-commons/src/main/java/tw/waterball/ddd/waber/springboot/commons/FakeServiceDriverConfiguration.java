package tw.waterball.ddd.waber.springboot.commons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.ddd.api.match.FakeMatchContext;
import tw.waterball.ddd.api.trip.FakeTripContext;
import tw.waterball.ddd.waber.api.payment.FakeUserContext;
import tw.waterball.ddd.waber.springboot.commons.profiles.FakeServiceDrivers;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@FakeServiceDrivers
@Configuration
public class FakeServiceDriverConfiguration {

    @Bean
    public FakeUserContext fakeUserServiceDriver() {
        return new FakeUserContext();
    }

    @Bean
    public FakeMatchContext fakeMatchServiceDriver() {
        return new FakeMatchContext();
    }

    @Bean
    public FakeTripContext fakeTripServiceDriver() {
        return new FakeTripContext();
    }
}
