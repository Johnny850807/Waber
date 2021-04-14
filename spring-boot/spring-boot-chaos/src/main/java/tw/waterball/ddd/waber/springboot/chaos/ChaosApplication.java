package tw.waterball.ddd.waber.springboot.chaos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tw.waterball.chaos.annotations.EnableChaosServer;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@EnableChaosServer
@SpringBootApplication(scanBasePackages = "tw.waterball.ddd")
public class ChaosApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChaosApplication.class, args);
    }
}
