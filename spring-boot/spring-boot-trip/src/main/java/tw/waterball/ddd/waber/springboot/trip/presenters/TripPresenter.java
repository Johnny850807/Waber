package tw.waterball.ddd.waber.springboot.trip.presenters;

import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.model.trip.Trip;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class TripPresenter implements tw.waterball.ddd.waber.trip.usecases.TripPresenter {
    private TripView tripView;
    @Override
    public void present(Trip trip) {
        tripView = TripView.toViewModel(trip);
    }

    public TripView getTripView() {
        return tripView;
    }
}
