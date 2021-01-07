package tw.waterball.ddd.waber.springboot.user.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.driver.usecases.SignUpToBeDriver;
import tw.waterball.ddd.waber.user.usecases.QueryDrivers;

import java.util.Collection;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@RequestMapping("/api/drivers")
@RestController
@AllArgsConstructor
public class DriverController {
    private SignUpToBeDriver signUpToBeDriver;
    private QueryDrivers queryDrivers;

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
    public Collection<Driver> queryDrivers(@RequestParam String activityName,
                                           @RequestParam Driver.CarType carType) {
        return queryDrivers.execute(new QueryDrivers.Request(activityName, carType));
    }

}
