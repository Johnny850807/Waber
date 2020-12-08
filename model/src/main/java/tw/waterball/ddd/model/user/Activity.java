package tw.waterball.ddd.model.match;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.AggregateRoot;
import tw.waterball.ddd.model.user.Driver;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
@NoArgsConstructor
public class Activity extends AggregateRoot {
    private int id;

    @Size(min = 1, max = 10)
    private String name;

    private Set<Driver> participantDrivers = new HashSet<>();

    public Activity(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Activity(String name) {
        this.name = name;
    }

    public void participate(Driver driver) {
        participantDrivers.add(driver);
    }

    public void clearParticipants() {
        participantDrivers.clear();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
