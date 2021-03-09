package tw.waterball.ddd.stubs;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class UserStubs {
    public static Passenger NORMAL_PASSENGER = new Passenger(20, "User", "user@email.com", new Location(400, 50));
    public static Driver NORMAL_DRIVER = new Driver(10, "Driver", "driver@email.com",
            Driver.CarType.Business,
            new Location(300, 50), Driver.Status.AVAILABLE);
}
