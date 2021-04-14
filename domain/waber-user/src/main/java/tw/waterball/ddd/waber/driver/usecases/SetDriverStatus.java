package tw.waterball.ddd.waber.driver.usecases;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.inject.Named;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
@Named
public class SetDriverStatus {
    private final UserRepository userRepository;

    @WithSpan
    public void execute(Request req) {
        Driver driver = (Driver) userRepository.findById(req.driverId).orElseThrow(NotFoundException::new);
        driver.setStatus(req.status);
        User saved = userRepository.save(driver);
        System.out.println();
    }

    @AllArgsConstructor
    public static class Request {
        public int driverId;
        public Driver.Status status;
    }
}
