package tw.waterball.ddd.model.match;

import tw.waterball.ddd.model.base.AggregateRoot;
import tw.waterball.ddd.model.user.Driver;

import java.util.*;

import static java.util.Optional.ofNullable;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Match extends AggregateRoot<Integer> {
    private final int passengerId;
    private Integer driverId;
    private final MatchPreferences preferences;
    private boolean alive = true;
    private Date createdDate = new Date();

    public static Match start(int passengerId, MatchPreferences preferences) {
        return new Match(passengerId, preferences);
    }

    public Match(Integer id, int passengerId, Integer driverId, MatchPreferences preferences, Date createdDate, boolean alive) {
        super(id);
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.preferences = preferences;
        this.createdDate = createdDate;
        this.alive = alive;
    }

    public Match(Integer id, int passengerId, Integer driverId, MatchPreferences preferences) {
        super(id);
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.preferences = preferences;
    }

    private Match(int passengerId, MatchPreferences preferences) {
        this.passengerId = passengerId;
        this.preferences = preferences;
    }

    public void cancel() {
        driverId = null;
    }

    public void matchDriver(Driver driver) {
        setDriverId(driver.getId());
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isPending() {
        return driverId == null;
    }

    public boolean isCompleted() {
        return !isPending();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public Optional<Integer> mayHaveDriverId() {
        return ofNullable(driverId);
    }
    public MatchPreferences getPreferences() {
        return preferences;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
}
