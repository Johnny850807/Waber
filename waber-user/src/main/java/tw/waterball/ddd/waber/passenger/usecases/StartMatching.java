package tw.waterball.ddd.waber.passenger.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.passenger.repositories.PassengerRepository;

import javax.inject.Named;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
public class StartMatching extends PassengerUsaCase {

    public StartMatching(PassengerRepository passengerRepository) {
        super(passengerRepository);
    }

    public Passenger execute(Request req) {
        Passenger passenger = getPassengerById(req.passengerId);
        passenger.startMatching(req.matchPreferences);
        return passengerRepository.save(passenger);
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId;
        public MatchPreferences matchPreferences;
    }
}
