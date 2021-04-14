package tw.watewrball.waber.springboot.chaos.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tw.waterball.waber.chaos.annotations.EnableChaosServer;

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
