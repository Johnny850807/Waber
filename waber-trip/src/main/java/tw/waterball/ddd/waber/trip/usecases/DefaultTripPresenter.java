package tw.waterball.ddd.waber.trip.usecases;

import lombok.Getter;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Getter
public class DefaultTripPresenter implements TripPresenter {
    private Match match;
    private Trip trip;
    @Override
    public void present(Match match, Trip trip) {
        this.match = match;
        this.trip = trip;
    }
}
