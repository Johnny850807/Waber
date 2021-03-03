package tw.waterball.ddd.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tw.waterball.ddd.model.match.Match;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@ToString
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
}
