package tw.waterball.ddd.waber.springboot.broker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@SpringBootApplication(scanBasePackages = "tw.waterball.ddd")
public class BrokerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrokerApplication.class, args);
    }
}
