package tw.waterball.ddd.model.journey;

import lombok.Getter;
import lombok.Setter;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.Price;
import tw.waterball.ddd.model.journey.states.Picking;
import tw.waterball.ddd.model.match.Match;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
@Setter
public class Journey {
    private final Match match;
    private JourneyState state = new Picking(this);
    private Location destination;
    private Price price;

    public Journey(Match match) {
        this.match = match;
    }

    public void pickUp() {
        state.pickUp();
    }

    public void startDriving(Location destination) {
        state.startDriving(destination);
    }

    public void refusePassenger() {
        state.refusePassenger();
    }

    public void arrive() {
        state.arrive();
    }

    public void pay(Price price) {
        this.price = price;
    }

    public boolean isPaid() {
        return price != null;
    }

}
