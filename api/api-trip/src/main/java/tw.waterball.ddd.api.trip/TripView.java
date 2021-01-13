package tw.waterball.ddd.api.trip;

import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.Price;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.model.trip.TripState;
import tw.waterball.ddd.model.trip.TripStateType;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@NoArgsConstructor
public class TripView {
    public String id;
    public Integer matchId;
    public Location destination;
    public Price price;
    public TripStateType state;

    public TripView(String id, Integer matchId, Location destination, Price price, TripState state) {
        this.id = id;
        this.matchId = matchId;
        this.destination = destination;
        this.price = price;
        this.state = state.getType();
    }

    public static TripView toViewModel(Trip trip) {
        return new TripView(trip.getId(), trip.getMatch().getId(),
                trip.getDestination(), trip.getPrice(), trip.getState());
    }
}
