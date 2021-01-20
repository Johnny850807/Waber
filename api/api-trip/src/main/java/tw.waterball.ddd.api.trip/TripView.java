package tw.waterball.ddd.api.trip;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.model.trip.TripState;
import tw.waterball.ddd.model.trip.TripStateType;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@EqualsAndHashCode
@NoArgsConstructor
public class TripView {
    public String id;
    public Integer matchId;
    public Location destination;
    public TripStateType state;

    public TripView(String id, Integer matchId, Location destination, TripState state) {
        this.id = id;
        this.matchId = matchId;
        this.destination = destination;
        this.state = state.getType();
    }

    public static TripView toViewModel(Trip trip) {
        return new TripView(trip.getId(), trip.getMatchAssociation().getId(),
                trip.getDestination(), trip.getState());
    }

    public Trip toEntityWithMatch(Match match) {
        Trip trip = new Trip(match);
        trip.setId(id);
        trip.setDestination(destination);
        trip.setState(state.toState(trip));
        return trip;
    }
}
