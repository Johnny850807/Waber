package tw.waterball.ddd.waber.springboot.user.controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.user.repositories.UserRepository;
import tw.waterball.ddd.waber.user.usecases.SignIn;
import tw.waterball.ddd.waber.user.usecases.UpdateLatestLocation;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
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
        return "OK";
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    @PostMapping("/signIn")
    public User signIn(@RequestBody SignInParams params) {
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
        updateLatestLocation.execute(new UpdateLatestLocation.Request(
                userId, new Location(latitude, longitude)
        ), eventBus);
    }


}
