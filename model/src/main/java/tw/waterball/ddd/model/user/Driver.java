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
        super(id);
    }

    public Driver(int id, String name, String email, String password, CarType carType, Location latestLocation) {
        super(id, name, email, password, latestLocation);
        this.carType = carType;
    }

    public Driver(String name, String email, String password, CarType carType) {
        super(name, email, password);
        this.carType = carType;
    }

    public void setStatus(Status status) {
        if (this.status == Status.MATCHED && status == Status.MATCHED) {
            throw new DriverHasBeenMatchedException();
        }
        this.status = status;
    }
}
