package tw.waterball.ddd.model.trip;

import tw.waterball.ddd.model.trip.states.Arrived;
import tw.waterball.ddd.model.trip.states.Driving;
import tw.waterball.ddd.model.trip.states.Picking;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public enum TripStateType {
    PICKING, DRIVING, ARRIVED;

    public TripState toState(Trip trip) {
        switch (this) {
            case PICKING:
                return new Picking(trip);
            case DRIVING:
                return new Driving(trip);
            case ARRIVED:
                return new Arrived(trip);
        }
        throw new InternalError("Impossibly reached.");
    }
}
