package tw.waterball.ddd.waber.springboot.trip.repositories.jpa;

import lombok.*;
import tw.waterball.ddd.model.Price;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.model.trip.TripState;
import tw.waterball.ddd.model.trip.TripStateType;

import javax.persistence.*;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripData {
    @Id
    public String id;
    public Integer matchId;
    public Location destination;
    public Double price;
    public TripStateType state;

    public TripData(String id, Integer matchId, Location destination,
                    Price price, TripState state) {
        this.id = id;
        this.matchId = matchId;
        this.destination = destination;
        this.price = price.getPrice();
        this.state = state.getType();
    }

    public static TripData toData(Trip trip) {
        return new TripData(trip.getId(), trip.getMatch().getId(),
                trip.getDestination(), trip.getPrice(), trip.getState());
    }

    public Trip toEntity() {
        Price price = new Price(this.price);
        return new Trip(id, matchId, destination, price, state);
    }
}
