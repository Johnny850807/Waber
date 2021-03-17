package tw.waterball.ddd.waber.user.usecases;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @WithSpan
    public User execute(Request req) {
        return userRepository.findByEmailAndPassword(req.email, req.password)
                .orElseThrow(NotFoundException::new);
    }

    @AllArgsConstructor
    public static class Request {
        public String email, password;
    }
}
