package tw.waterball.ddd.waber.springboot.trip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "tw.waterball.ddd")
public class TripApplication {
    public static void main(String[] args) {
        SpringApplication.run(TripApplication.class, args);
    }
}
