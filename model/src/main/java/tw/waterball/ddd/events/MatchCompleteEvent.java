package tw.waterball.ddd.events;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.model.user.Driver;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@AllArgsConstructor
public class MatchCompleteEvent {
    public int passengerId;
    public Driver driver;
}
