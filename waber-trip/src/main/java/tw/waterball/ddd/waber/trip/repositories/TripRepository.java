package tw.waterball.ddd.waber.trip.repositories;

import tw.waterball.ddd.model.trip.Trip;

import java.util.List;
import java.util.Optional;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface TripRepository {
    List<Trip> queryDriverTripHistory(int driverId);

    List<Trip> queryPassengerTripHistory(int passengerId);

    Trip save(Trip trip);

    Optional<Trip> findById(String id);

    void clearAll();
}
