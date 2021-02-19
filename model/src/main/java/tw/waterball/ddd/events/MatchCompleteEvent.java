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
    public static final String EVENT_NAME = "MatchCompleteEvent";
    private int passengerId;
    private int driverId;

    public MatchCompleteEvent(Match match) {
        this(match.getPassengerId(), match.getDriverId());
    }

    public MatchCompleteEvent(int passengerId, int driverId) {
        super(EVENT_NAME);
        this.passengerId = passengerId;
        this.driverId = driverId;
    }
}
