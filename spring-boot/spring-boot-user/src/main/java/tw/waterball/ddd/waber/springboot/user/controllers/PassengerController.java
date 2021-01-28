package tw.waterball.ddd.waber.springboot.user.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.driver.usecases.SignUpToBeDriver;
import tw.waterball.ddd.waber.passenger.usecases.SignUpToBePassenger;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@CrossOrigin
@RequestMapping("/api/passengers")
@RestController
@AllArgsConstructor
public class PassengerController {
    private SignUpToBePassenger signUpToBePassenger;

    @PostMapping
    public Passenger signUp(@RequestBody SignUpParams params) {
        return signUpToBePassenger.execute(
                new SignUpToBePassenger.Request(params.name, params.email, params.password)
        );
    }

    public static class SignUpParams {
        public String name, email, password;
    }

}
