package tw.waterball.ddd.waber.springboot.trip.repositories.jpa;

import org.springframework.stereotype.Component;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.waber.trip.repositories.TripRepository;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Component
public class SpringBootTripRepository implements TripRepository {
    private MongoTripDataPort tripDataPort;

    public SpringBootTripRepository(MongoTripDataPort tripDataPort) {
        this.tripDataPort = tripDataPort;
    }

    @Override
    public Trip save(Trip trip) {
        TripData data = TripData.toData(trip);
        TripData saveData = tripDataPort.save(data);
        return saveData.toEntity();
    }
}
