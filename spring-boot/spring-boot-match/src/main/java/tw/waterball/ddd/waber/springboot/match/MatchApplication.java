package tw.waterball.ddd.waber.springboot.match;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.model.Jobs;
import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.waber.api.payment.UserServiceDriver;
import tw.waterball.ddd.waber.match.domain.PerformMatch;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;
import tw.waterball.ddd.waber.match.usecases.FindCurrentMatch;
import tw.waterball.ddd.waber.match.usecases.MatchUseCase;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = "tw.waterball.ddd")
public class MatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchApplication.class, args);
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService(
            @Value("${waber.match.schedule.corePool}") int corePoolSize) {
        return Executors.newScheduledThreadPool(corePoolSize);
    }

    @Bean
    public Jobs jobs(ScheduledExecutorService scheduledExecutorService) {
        return new Jobs(scheduledExecutorService);
    }

    @Bean
    public MatchUseCase matchingUseCase(UserServiceDriver userServiceDriver,
                                        MatchRepository matchRepository,
                                        PerformMatch performMatch,
                                        FindCurrentMatch findCurrentMatch,
                                        EventBus eventBus,
                                        @Value("${waber.match.schedule.rescheduleDelayTimeInMs}") long rescheduleDelayTimeInMs) {
        return new MatchUseCase(userServiceDriver, matchRepository,
                findCurrentMatch, performMatch, rescheduleDelayTimeInMs, eventBus);
    }

}
