package tw.waterball.ddd.waber.passenger.repositories;

import tw.waterball.ddd.model.user.Passenger;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface PassengerRepository {

    Optional<Passenger> findById(int id);

    Passenger save(Passenger passenger);
}
