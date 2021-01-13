package tw.waterball.ddd.model.trip;

import lombok.Getter;
import lombok.Setter;
import tw.waterball.ddd.model.associations.One;
import tw.waterball.ddd.model.base.AggregateRoot;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.Price;
import tw.waterball.ddd.model.trip.states.Picking;
import tw.waterball.ddd.model.match.Match;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
@Setter
public class Trip extends AggregateRoot<String> {
    private final One<Match> match;
    private TripState state = new Picking(this);
    private Location destination;
    private Price price = Price.unsolved();

    public Trip(Match match) {
        this.match = One.of(match);
    }

    public Trip(Match match, TripState state) {
        this.match = One.of(match);
        this.state = state;
    }

    public Trip(String tripId, int matchId, Location destination, Price price, TripStateType type) {
        this.id = tripId;
        this.match = One.associate(matchId);
        this.destination = destination;
        this.price = price;
        this.state = type.toState(this);
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
