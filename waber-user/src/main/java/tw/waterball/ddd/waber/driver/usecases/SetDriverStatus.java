package tw.waterball.ddd.waber.driver.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.inject.Named;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
@Named
public class SetDriverStatus {
    private UserRepository userRepository;

    public void execute(Request req) {
        Driver driver = (Driver) userRepository.associateById(req.driverId);
        driver.setStatus(req.status);
        userRepository.save(driver);
    }

    @AllArgsConstructor
    public static class Request {
        public int driverId;
        public Driver.Status status;
    }
}
