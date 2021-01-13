package tw.waterball.ddd.waber.springboot.trip.repositories.jpa;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface MongoTripDataPort extends MongoRepository<TripData, Integer> {
}
