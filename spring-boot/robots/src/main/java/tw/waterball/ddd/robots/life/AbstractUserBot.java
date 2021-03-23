package tw.waterball.ddd.robots.life;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Driver;

import java.util.Optional;
import java.util.Random;

/**
 * Share codes among its subclasses
 * @author Waterball (johnny850807@gmail.com)
 */
public abstract class AbstractUserBot extends Life {
    protected Random random = new Random();

    protected Location randomLocation() {
        return new Location(random.nextInt(501) - 250, random.nextInt(501) - 250);
    }

    protected Driver.CarType randomCarType() {
        Driver.CarType[] carTypes = Driver.CarType.values();
//        return carTypes[random.nextInt(carTypes.length)];
        return Driver.CarType.Normal; // fixed return for debugging
    }

    public abstract Optional<Location> getLocation();
}
