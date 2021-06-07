package tw.waterball.ddd.events;

import static tw.waterball.ddd.commons.utils.MapUtils.map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
public class StartMatchingCommand extends Event {
    public static final String NAME = "StartMatching";
    private int matchId;
    private int passengerId;

    public StartMatchingCommand(int matchId, int passengerId) {
        super(NAME);
        this.matchId = matchId;
        this.passengerId = passengerId;
    }

    @Override
    public Map<String, String> toMap() {
        return map("passengerId", "matchId")
                .to(String.valueOf(passengerId), String.valueOf(matchId));
    }
}
