package tw.waterball.ddd.waber.trip.repositories;

import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;

import java.util.List;
import java.util.Optional;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface TripRepository {
    List<Trip> queryDriverTripHistory(int driverId);

    List<Trip> queryPassengerTripHistory(int passengerId);

    Trip saveTripWithMatch(Trip trip, Match match);

    Optional<Trip> findById(String id);

    Optional<Trip> findByMatchId(int matchId);

    void clearAll();
}
