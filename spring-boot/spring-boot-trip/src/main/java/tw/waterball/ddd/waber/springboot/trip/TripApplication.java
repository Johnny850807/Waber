package tw.waterball.ddd.waber.springboot.trip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@SpringBootApplication(scanBasePackages = "tw.waterball.ddd")
public class TripApplication {
    public static void main(String[] args) {
        SpringApplication.run(TripApplication.class, args);
    }
}
