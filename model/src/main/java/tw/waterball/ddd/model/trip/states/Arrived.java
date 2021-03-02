package tw.waterball.ddd.model.trip.states;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.model.trip.TripState;
import tw.waterball.ddd.model.trip.TripStateType;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Arrived extends TripState {
    public Arrived(Trip trip) {
        super(TripStateType.ARRIVED, trip);
    }

    @Override
    public void startDriving(Location destination) {
        throw new IllegalStateException("The trip has been over.");
    }

    @Override
    public void refusePassenger() {
        throw new IllegalStateException("The trip has been over.");
    }

    @Override
    public void arrive() {
        throw new IllegalStateException("The trip has been over.");
    }
}
