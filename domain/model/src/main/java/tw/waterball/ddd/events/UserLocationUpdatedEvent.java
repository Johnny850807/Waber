package tw.waterball.ddd.events;

import static tw.waterball.ddd.commons.utils.MapUtils.map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tw.waterball.ddd.model.geo.Location;

import java.util.Map;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
public class UserLocationUpdatedEvent extends Event  {
    public final static String NAME = "UserLocationUpdated";
    private int userId;
    private Location location;

    public UserLocationUpdatedEvent(int userId, Location location) {
        super(NAME);
        this.userId = userId;
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("userId=%d latitude=%f longitude=%f",
                userId, location.getLatitude(), location.getLongitude());
    }

    @Override
    public Map<String, String> toMap() {
        return map("userId", "latitude", "longitude")
                .to(String.valueOf(userId), String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()));
    }
}
