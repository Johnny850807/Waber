package tw.waterball.ddd.waber.user.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.inject.Named;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
public class SignIn {
    private final UserRepository userRepository;

    public SignIn(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Request req) {
        return userRepository.findByEmailAndPassword(req.email, req.password)
                .orElseThrow(NotFoundException::new);
    }

    @AllArgsConstructor
    public static class Request {
        public String email, password;
    }
}
