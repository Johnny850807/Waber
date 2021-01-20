package tw.waterball.ddd.waber.springboot.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@SpringBootApplication(scanBasePackages = "tw.waterball.ddd")
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}
