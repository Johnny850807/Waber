package tw.waterball.ddd.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.Match;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
@NoArgsConstructor
public class Passenger extends User {
    private Match match;

    public Passenger(int id, String name, String email, String password, Location latestLocation) {
        super(id, name, email, password, latestLocation);
    }

    public Passenger(String name, String email, String password) {
        super(name, email, password);
    }

}
