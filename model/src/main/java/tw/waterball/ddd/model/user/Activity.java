package tw.waterball.ddd.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.base.AggregateRoot;
import tw.waterball.ddd.model.associations.Many;

import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
@NoArgsConstructor
public class Activity extends AggregateRoot<Integer> {
    @Size(min = 1, max = 10)
    private String name;

    private Many<Driver> participantDrivers = new Many<>();

    public Activity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Activity(String name) {
        this.name = name;
    }

    public boolean hasParticipant(Driver driver) {
        return getParticipantDrivers().contains(driver);
    }

    public Collection<Driver> getParticipantDrivers() {
        return participantDrivers.get();
    }

    public void setParticipantDrivers(Many<Driver> participantDrivers) {
        this.participantDrivers = participantDrivers;
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
