package tw.waterball.ddd.waber.springboot.trip.repositories.jpa;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface MongoTripDataPort extends MongoRepository<TripData, String> {
    List<TripData> findAllByPassengerId(int passengerId);

    List<TripData> findAllByDriverId(int driverId);

    Optional<TripData> findByMatchId(int matchId);
}
