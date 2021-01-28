package tw.waterball.ddd.waber.passenger.usecases;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.inject.Named;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
@AllArgsConstructor
public class SignUpToBePassenger {
    private UserRepository userRepository;

    public Passenger execute(Request req) {
        Passenger passenger = new Passenger(req.name, req.email, req.password);
        passenger.validate();
        return (Passenger) userRepository.save(passenger);
    }

    @AllArgsConstructor
    public static class Request {
        public String name;
        public String email;
        public String password;
    }

}
