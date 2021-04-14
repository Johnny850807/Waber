package tw.waterball.ddd.waber.springboot.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tw.waterball.waber.chaos.annotations.EnableChaosClient;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@EnableChaosClient
@SpringBootApplication(scanBasePackages = "tw.waterball.ddd")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
