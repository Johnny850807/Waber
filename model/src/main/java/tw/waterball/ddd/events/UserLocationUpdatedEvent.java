package tw.waterball.ddd.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tw.waterball.ddd.model.geo.Location;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
public class UserLocationUpdatedEvent extends Event  {
    public final static String EVENT_NAME = "UserLocationUpdated";
    private int userId;
    private Location location;

    public UserLocationUpdatedEvent(int userId, Location location) {
        super(EVENT_NAME);
        this.userId = userId;
        this.location = location;
    }
}
