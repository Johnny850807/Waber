package tw.waterball.ddd.waber.driver.usecases;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.inject.Named;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
@AllArgsConstructor
public class SignUpToBeDriver {
    private final UserRepository userRepository;

    @WithSpan
    public Driver execute(Request req) {
        Driver driver = new Driver(req.name, req.email, req.password, req.carType);
        driver.validate();
        return (Driver) userRepository.saveUserWithHisPassword(driver, req.password);
    }

    @AllArgsConstructor
    public static class Request {
        public String name;
        public String email;
        public String password;
        public Driver.CarType carType;
    }

}
