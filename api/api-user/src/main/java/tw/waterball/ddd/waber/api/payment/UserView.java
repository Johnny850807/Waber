package tw.waterball.ddd.waber.api.payment;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.User;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserView {
    public int id;
    public String type;
    public String name;
    public String email;
    public Location location;

    public static UserView toViewModel(User user) {
        return new UserView(user.getId(), user.getType(),
                user.getName(), user.getEmail(), user.getLocation());
    }
}
