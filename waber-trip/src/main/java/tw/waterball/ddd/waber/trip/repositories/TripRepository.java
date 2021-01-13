package tw.waterball.ddd.waber.trip.repositories;

import tw.waterball.ddd.model.trip.Trip;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface TripRepository {
    Trip save(Trip trip);
}
