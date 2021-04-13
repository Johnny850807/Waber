package tw.waterball.ddd.waber.springboot.trip.repositories.jpa;

import org.springframework.stereotype.Component;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.waber.trip.repositories.TripRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Component
public class SpringBootTripRepository implements TripRepository {
    private final TripDAO tripDAO;

    public SpringBootTripRepository(TripDAO tripDAO) {
        this.tripDAO = tripDAO;
    }

    @Override
    public List<Trip> queryDriverTripHistory(int driverId) {
        return tripDAO.findAllByDriverId(driverId).stream()
                .map(TripData::toEntity)
                .collect(Collectors.toList());
    }
    @Override
    public List<Trip> queryPassengerTripHistory(int passengerId) {
        return tripDAO.findAllByPassengerId(passengerId).stream()
                .map(TripData::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Trip saveTripWithMatch(Trip trip, Match match) {
        TripData data = TripData.toData(trip, match);
        TripData saveData = tripDAO.save(data);
        return saveData.toEntity();
    }

    @Override
    public Optional<Trip> findById(String id) {
        return tripDAO.findById(id)
                .map(TripData::toEntity);
    }

    @Override
    public Optional<Trip> findByMatchId(int matchId) {
        return tripDAO.findByMatchId(matchId)
                .map(TripData::toEntity);
    }

    @Override
    public void clearAll() {
        tripDAO.deleteAll();
    }
}
