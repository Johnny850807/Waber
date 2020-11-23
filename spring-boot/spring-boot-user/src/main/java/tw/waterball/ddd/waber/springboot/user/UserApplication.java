package tw.waterball.ddd.waber.springboot.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@SpringBootApplication(scanBasePackages = "tw.waterball.ddd")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
