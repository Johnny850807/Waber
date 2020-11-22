package tw.waterball.ddd.model.journey.states;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.journey.Journey;
import tw.waterball.ddd.model.journey.JourneyState;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Arrived extends JourneyState {
    public Arrived(Journey journey) {
        super(journey);
    }

    @Override
    public void pickUp() {

    }
    @Override
    public void startDriving(Location destination) {

    }

    public void refusePassenger() {

    }

    public void arrive() {

    }
}
