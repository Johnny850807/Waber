package tw.waterball.ddd.waber.passenger.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.passenger.repositories.PassengerRepository;

import javax.inject.Named;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
public class SignUpToBePassenger extends PassengerUsaCase {

    public SignUpToBePassenger(PassengerRepository passengerRepository) {
        super(passengerRepository);
    }

    public Passenger execute(Request req) {
        Passenger passenger = new Passenger(req.name, req.email, req.password);
        return passengerRepository.save(passenger);
    }

    @AllArgsConstructor
    public static class Request {
        public String name;
        public String email;
        public String password;
    }

}
