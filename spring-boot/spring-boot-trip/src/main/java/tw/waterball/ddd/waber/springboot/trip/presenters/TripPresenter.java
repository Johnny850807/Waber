package tw.waterball.ddd.waber.springboot.trip.presenters;

import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class TripPresenter implements tw.waterball.ddd.waber.trip.usecases.TripPresenter {
    private TripView tripView;

    public TripView getTripView() {
        return tripView;
    }

    @Override
    public void present(Match match, Trip trip) {
        tripView = TripView.toViewModel(trip);
    }
}
