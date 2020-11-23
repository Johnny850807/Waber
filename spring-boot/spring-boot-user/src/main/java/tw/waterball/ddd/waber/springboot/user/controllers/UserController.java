package tw.waterball.ddd.waber.springboot.user.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.driver.usecases.SignUpToBeDriver;
import tw.waterball.ddd.waber.passenger.usecases.SignUpToBePassenger;
import tw.waterball.ddd.waber.user.usecases.UpdateLatestLocation;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@RequestMapping("/api/users")
@RestController
public class UserController {
    private SignUpToBeDriver signUpToBeDriver;
    private SignUpToBePassenger signUpToBePassenger;
    private UpdateLatestLocation updateLatestLocation;

    public UserController(SignUpToBeDriver signUpToBeDriver,
                          SignUpToBePassenger signUpToBePassenger,
                          UpdateLatestLocation updateLatestLocation) {
        this.signUpToBeDriver = signUpToBeDriver;
        this.signUpToBePassenger = signUpToBePassenger;
        this.updateLatestLocation = updateLatestLocation;
    }

    @PostMapping
    public User signUp(@RequestParam("type") String type,
                       @RequestBody SignUpParams params) {
        type = type.toLowerCase();
        if (type.equals("passenger")) {
            return signUpToBePassenger.execute(new SignUpToBePassenger.Request(
                    params.name, params.email, params.password));
        } else if (type.equals("driver")) {
            Driver.CarType carType = Driver.CarType.valueOf(params.carType.toUpperCase());
            return signUpToBeDriver.execute(new SignUpToBeDriver.Request(
                    params.name, params.email, params.password, carType
            ));
        } else {
            throw new IllegalArgumentException("Passenger type " + type + " not supported.");
        }
    }

    public static class SignUpParams {
        public String name, email, password, carType;
    }

    @PutMapping("/{userId}/location")
    public void updateLatestLocation(@PathVariable int userId,
                                     @RequestParam double latitude,
                                     @RequestParam double longitude) {
        updateLatestLocation.execute(new UpdateLatestLocation.Request(
                userId, new Location(latitude, longitude)
        ));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleException() {
    }
}
