package tw.waterball.ddd.waber.trip.usecases;

import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface TripPresenter {
    void present(Match match, Trip trip);
}
