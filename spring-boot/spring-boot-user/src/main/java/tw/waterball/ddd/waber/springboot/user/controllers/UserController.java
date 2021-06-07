package tw.waterball.ddd.waber.springboot.user.controllers;

import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.attr;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.currentSpan;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.event;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.user.repositories.UserRepository;
import tw.waterball.ddd.waber.user.usecases.SignIn;
import tw.waterball.ddd.waber.user.usecases.UpdateLatestLocation;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final SignIn signIn;
    private final UpdateLatestLocation updateLatestLocation;
    private final UserRepository userRepository;
    private final EventBus eventBus;

    @GetMapping("/health")
    public String healthCheck() {
        log.info("Health Check.");
        return "OK";
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

    @PostMapping("/signIn")
    public User signIn(@RequestBody SignInParams params) {
        currentSpan(event("SignIn"), attr("email", params.email)).asLog(log::info);

        return signIn.execute(new SignIn.Request(params.email, params.password));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignInParams {
        public String email, password;
    }

    @PutMapping("/{userId}/location")
    public void updateLatestLocation(@PathVariable int userId,
                                     @RequestParam double latitude,
                                     @RequestParam double longitude) {
        try {
            updateLatestLocation.execute(new UpdateLatestLocation.Request(
                    userId, new Location(latitude, longitude)
            ));
        } catch (Exception err) {
            err.printStackTrace();
        }
    }


}
