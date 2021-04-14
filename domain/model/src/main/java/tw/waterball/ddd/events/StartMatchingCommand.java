package tw.waterball.ddd.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
public class StartMatchingCommand extends Event {
    public static final String NAME = "StartMatchingCommand";
    private int matchId;
    private int passengerId;

    public StartMatchingCommand(int matchId, int passengerId) {
        super(NAME);
        this.matchId = matchId;
        this.passengerId = passengerId;
    }
}
