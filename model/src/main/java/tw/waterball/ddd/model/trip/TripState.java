package tw.waterball.ddd.model.trip;

import tw.waterball.ddd.model.geo.Location;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class TripState {
    public TripStateType type;
    protected Trip journey;

    public TripState(TripStateType type, Trip trip) {
        this.type = type;
        this.journey = trip;
    }

    public TripStateType getType() {
        return type;
    }

    public abstract void startDriving(Location destination);

    public abstract void refusePassenger();

    public abstract void arrive();
}
