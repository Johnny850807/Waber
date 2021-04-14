package tw.waterball.ddd.model.trip.states;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.model.trip.TripState;
import tw.waterball.ddd.model.trip.TripStateType;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Driving extends TripState {
    public Driving(Trip trip) {
        super(TripStateType.DRIVING, trip);
    }

    @Override
    public void startDriving(Location destination) {
        trip.setDestination(destination);
    }

    @Override
    public void refusePassenger() {
        // TODO
    }

    @Override
    public void arrive() {
        trip.setState(new Arrived(trip));
    }
}
