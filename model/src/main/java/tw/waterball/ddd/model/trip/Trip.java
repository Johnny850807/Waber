package tw.waterball.ddd.model.trip;

import tw.waterball.ddd.model.associations.One;
import tw.waterball.ddd.model.base.AggregateRoot;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.states.Picking;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Trip extends AggregateRoot<String> {
    private final int matchId;
    private TripState state = new Picking(this);
    private Location destination;

    public Trip(int matchId) {
        this.matchId = matchId;
    }

    public Trip(String id, int matchId) {
        this.id = id;
        this.matchId = matchId;
    }

    public Trip(String tripId, int matchId, Location destination, TripStateType type) {
        this.id = tripId;
        this.matchId = matchId;
        this.destination = destination;
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

    public TripState getState() {
        return state;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void setState(TripState state) {
        this.state = state;
    }

    public int getMatchId() {
        return matchId;
    }

}
