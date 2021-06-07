package tw.waterball.ddd.events;

import static tw.waterball.ddd.commons.utils.MapUtils.map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.model.trip.TripState;
import tw.waterball.ddd.model.trip.TripStateType;

import java.util.Map;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
public class TripStateChangedEvent extends Event {
    public static final String NAME = "TripArrivedEvent";
    private int driverId;
    private int passengerId;
    private int matchId;
    private String tripId;
    private TripStateType state;

    public TripStateChangedEvent(Match match, Trip trip) {
        this(match.getDriverId(), match.getPassengerId(), match.getId(), trip.getId(), trip.getState());
    }

    public TripStateChangedEvent(int driverId, int passengerId, int matchId, String tripId, TripState state) {
        this(driverId, passengerId, matchId, tripId, state.getType());
    }

    public TripStateChangedEvent(int driverId, int passengerId, int matchId, String tripId, TripStateType state) {
        super(NAME);
        this.driverId = driverId;
        this.passengerId = passengerId;
        this.matchId = matchId;
        this.tripId = tripId;
        this.state = state;
    }

    @Override
    public String toString() {
        return String.format("driverId=%d passengerId=%d matchId=%d tripId=%s state=%s",
                driverId, passengerId, matchId, tripId, state);
    }

    @Override
    public Map<String, String> toMap() {
        return map("driverId", "passengerId", "matchId", "tripId", "state")
                .to(String.valueOf(driverId), String.valueOf(passengerId), String.valueOf(matchId),
                        String.valueOf(tripId), String.valueOf(state));
    }
}
