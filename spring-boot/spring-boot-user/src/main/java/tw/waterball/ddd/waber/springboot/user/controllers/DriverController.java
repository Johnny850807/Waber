package tw.waterball.ddd.waber.springboot.user.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.commons.model.ErrorMessage;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverIsNotAvailableException;
import tw.waterball.ddd.waber.driver.usecases.SetDriverStatus;
import tw.waterball.ddd.waber.driver.usecases.SignUpToBeDriver;
import tw.waterball.ddd.waber.user.usecases.FindAvailableDrivers;

import java.util.Collection;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@CrossOrigin
@RequestMapping("/api/drivers")
@RestController
@AllArgsConstructor
public class DriverController {
    private final SignUpToBeDriver signUpToBeDriver;
    private final FindAvailableDrivers findAvailableDrivers;
    private final SetDriverStatus setDriverStatus;

    @PostMapping
    public Driver signUp(@RequestBody SignUpParams params) {
        return signUpToBeDriver.execute(
                new SignUpToBeDriver.Request(params.name, params.email, params.password, params.carType)
        );
    }

    public static class SignUpParams {
        public String name, email, password;
        public Driver.CarType carType;
    }

    @GetMapping
    public Collection<Driver> queryAvailableDrivers(@RequestParam(required = false) String activityName,
                                           @RequestParam(required = false) Driver.CarType carType) {
        var drivers = findAvailableDrivers.execute(new FindAvailableDrivers.Request(activityName, carType));
        return drivers;
    }

    @PatchMapping("/{driverId}")
    public void setDriverStatus(@PathVariable int driverId,
                                @RequestBody String status) throws DriverIsNotAvailableException {
        setDriverStatus.execute(new SetDriverStatus.Request(driverId,
                Driver.Status.valueOf(status.trim().toUpperCase())));
    }

    // survey if this handler can be shared in spring-boot-commons
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DriverIsNotAvailableException.class})
    public ErrorMessage handleDriverHasBeenMatchedException(DriverIsNotAvailableException err) {
        return ErrorMessage.fromException(err);
    }

}
