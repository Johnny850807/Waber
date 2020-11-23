package tw.waterball.ddd.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tw.waterball.ddd.model.match.Match;

import javax.validation.constraints.NotNull;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Setter
@Getter
@NoArgsConstructor
public class Driver extends User {
    @NotNull
    private CarType carType;

    private Match match;

    public enum CarType {
        NORMAL, BUSINESS, SPORT
    }

    public Driver(int id, String name, String email, String password, CarType carType) {
        super(id, name, email, password);
        this.carType = carType;
    }

    public Driver(String name, String email, String password, CarType carType) {
        super(name, email, password);
        this.carType = carType;
    }

    public boolean isMatched() {
        return match != null;
    }
}
