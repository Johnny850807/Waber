package tw.waterball.ddd.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.Match;

import javax.validation.constraints.NotNull;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Setter
@Getter
@NoArgsConstructor
public class Driver extends User {
    public static final String TYPE = "Driver";
    private Status status = Status.AVAILABLE;
    @NotNull
    private CarType carType;

    public enum CarType {
        Normal, Business, Sport
    }

    public enum Status {
        AVAILABLE, MATCHED
    }

    public Driver(int id) {
        super(TYPE, id);
    }

    public Driver(int id, String name, String email, String password, CarType carType, Location latestLocation, Status status) {
        super(TYPE, id, name, email, password, latestLocation);
        this.carType = carType;
        this.status = status;
    }

    public Driver(String name, String email, String password, CarType carType) {
        super(TYPE, name, email, password);
        this.carType = carType;
    }

    public boolean isAvailable() {
        return true;
    }

    public void setStatus(Status status) {
        if (this.status == Status.MATCHED && status == Status.MATCHED) {
            throw new DriverHasBeenMatchedException(this);
        }
        this.status = status;
    }
}
