package tw.waterball.ddd.waber.springboot.match;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tw.waterball.ddd.model.Jobs;
import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;
import tw.waterball.ddd.waber.match.usecases.MatchingUseCase;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@EnableAspectJAutoProxy
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "tw.waterball.ddd")
public class MatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchApplication.class, args);
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService(
            @Value("${match.schedule.corePool}") int corePoolSize) {
        return Executors.newScheduledThreadPool(corePoolSize);
    }

    @Bean
    public Jobs jobs(ScheduledExecutorService scheduledExecutorService) {
        return new Jobs(scheduledExecutorService);
    }

    @Bean
    public MatchingUseCase matchingUseCase(Jobs matchingJobs,
                                           MatchRepository matchRepository,
                                           DistanceCalculator distanceCalculator,
                                           @Value("${match.schedule.rescheduleDelayTimeInMs}") long rescheduleDelayTimeInMs) {
        return new MatchingUseCase(matchingJobs, matchRepository, distanceCalculator, rescheduleDelayTimeInMs);
    }

}
