package tw.waterball.ddd.model.journey;

import tw.waterball.ddd.model.geo.Location;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class JourneyState {
    protected Journey journey;

    public JourneyState(Journey journey) {
        this.journey = journey;
    }

    public abstract void pickUp();

    public abstract void startDriving(Location destination);

    public abstract void refusePassenger();

    public abstract void arrive();
}
