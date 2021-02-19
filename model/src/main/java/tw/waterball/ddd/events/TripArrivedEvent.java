package tw.waterball.ddd.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
public class TripArrivedEvent extends Event {
    public static final String EVENT_NAME = "TripArrivedEvent";
    private int driverId;
    private int passengerId;
    private int matchId;
    private String tripId;

    public TripArrivedEvent(Match match, Trip trip) {
        this(match.getDriverId(), match.getPassengerId(), match.getId(), trip.getId());
    }

    public TripArrivedEvent(int driverId, int passengerId, int matchId, String tripId) {
        super(EVENT_NAME);
        this.driverId = driverId;
        this.passengerId = passengerId;
        this.matchId = matchId;
        this.tripId = tripId;
    }
}
