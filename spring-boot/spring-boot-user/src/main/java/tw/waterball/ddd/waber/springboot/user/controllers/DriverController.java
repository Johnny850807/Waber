package tw.waterball.ddd.waber.springboot.user.controllers;

import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.attr;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.currentSpan;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.commons.model.ErrorMessage;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverNotAvailableException;
import tw.waterball.ddd.waber.driver.usecases.SetDriverStatus;
import tw.waterball.ddd.waber.driver.usecases.SignUpToBeDriver;
import tw.waterball.ddd.waber.user.usecases.FindAvailableDrivers;

import java.util.Collection;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@CrossOrigin
@Slf4j
@RequestMapping("/api/drivers")
@RestController
@AllArgsConstructor
public class DriverController {
    private final SignUpToBeDriver signUpToBeDriver;
    private final FindAvailableDrivers findAvailableDrivers;
    private final SetDriverStatus setDriverStatus;

    @PostMapping
    public Driver signUp(@RequestBody SignUpParams params) {
        currentSpan(event("sign-up"),
                attr("name", params.name), attr("email", params.name), attr("carType", params.carType))
                .asLog(log::info);

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
        currentSpan(attr("activityName", activityName), attr("carType", carType));

        return findAvailableDrivers.execute(new FindAvailableDrivers.Request(activityName, carType));
    }

    @PatchMapping("/{driverId}")
    public void setDriverStatus(@PathVariable int driverId,
                                @RequestBody String status) throws DriverNotAvailableException {
        currentSpan(event("SetDriverStatus"), attr("driverId", driverId), attr("status", status))
                .asLog(log::info);

        setDriverStatus.execute(new SetDriverStatus.Request(driverId,
                Driver.Status.valueOf(status.trim().toUpperCase())));
    }

    // survey if this handler can be shared in spring-boot-commons
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DriverNotAvailableException.class})
    public ErrorMessage handleDriverHasBeenMatchedException(DriverNotAvailableException err) {
        return ErrorMessage.fromException(err);
    }

}
