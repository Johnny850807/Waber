package tw.waterball.ddd.model.trip;

import tw.waterball.ddd.model.geo.Location;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class TripState {
    protected TripStateType type;
    protected Trip trip;

    public TripState(TripStateType type, Trip trip) {
        this.type = type;
        this.trip = trip;
    }

    public TripStateType getType() {
        return type;
    }

    public abstract void startDriving(Location destination);

    public abstract void refusePassenger();

    public abstract void arrive();
}
