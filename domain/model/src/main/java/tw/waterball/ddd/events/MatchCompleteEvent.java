package tw.waterball.ddd.events;

import static tw.waterball.ddd.commons.utils.MapUtils.map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tw.waterball.ddd.commons.utils.MapUtils;
import tw.waterball.ddd.model.match.Match;

import java.util.Map;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
public class MatchCompleteEvent extends Event {
    public static final String NAME = "MatchCompleteEvent";
    private int passengerId;
    private int driverId;
    private int matchId;

    public MatchCompleteEvent(Match match) {
        this(match.getId(), match.getPassengerId(), match.getDriverId());
    }

    public MatchCompleteEvent(int matchId, int passengerId, int driverId) {
        super(NAME);
        this.matchId = matchId;
        this.passengerId = passengerId;
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return String.format("driverId=%d passengerId=%d matchId=%d",
                driverId, passengerId, matchId);
    }

    @Override
    public Map<String, String> toMap() {
        return map("driverId", "passengerId", "matchId")
                .to(String.valueOf(driverId), String.valueOf(passengerId), String.valueOf(matchId));
    }
}
