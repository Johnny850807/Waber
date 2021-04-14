package tw.waterball.ddd.model.trip.states;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.model.trip.TripState;
import tw.waterball.ddd.model.trip.TripStateType;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Picking extends TripState {
    public Picking(Trip trip) {
        super(TripStateType.PICKING, trip);
    }

    @Override
    public void startDriving(Location destination) {
        trip.setDestination(destination);
        trip.setState(new Driving(trip));
    }

    @Override
    public void refusePassenger() {
        // TODO
    }

    @Override
    public void arrive() {
        throw new IllegalStateException("Can only 'arrive' from driving state.");
    }
}
