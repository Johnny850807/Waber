package tw.waterball.ddd.robots.api;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tw.waterball.ddd.api.match.MatchContext;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.api.trip.TripContext;
import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverNotAvailableException;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.api.payment.UserContext;

import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
@Component
public class API implements UserContext, MatchContext, TripContext {
    private final UserContext userContext;
    private final MatchContext matchContext;
    private final TripContext tripContext;

    @Override
    public void uploadLocation(int userId, Location location) {
        userContext.uploadLocation(userId, location);
    }

    @Override
    public Driver signUpAsDriver(String name, String email, String password, Driver.CarType carType) {
        return userContext.signUpAsDriver(name, email, password, carType);
    }

    @Override
    public Passenger signUpAsPassenger(String name, String email, String password) {
        return userContext.signUpAsPassenger(name, email, password);
    }

    @Override
    public Driver getDriver(int driverId) {
        return userContext.getDriver(driverId);
    }

    @Override
    public void changeDriverStatus(int driverId, Driver.Status status) throws DriverNotAvailableException {
        userContext.changeDriverStatus(driverId, status);
    }

    @Override
    public Passenger getPassenger(int passengerId) {
        return userContext.getPassenger(passengerId);
    }

    @Override
    public List<Driver> getDrivers(MatchPreferences matchPreferences) {
        return userContext.getDrivers(matchPreferences);
    }

    @Override
    public User getUser(int userId) {
        return userContext.getUser(userId);
    }

    @Override
    public MatchView startMatching(int userId, MatchPreferences matchPreferences) {
        return matchContext.startMatching(userId, matchPreferences);
    }

    @Override
    public MatchView getMatch(int matchId) {
        return matchContext.getMatch(matchId);
    }


    @Override
    public MatchView getCurrentMatch(int userId) {
        return matchContext.getCurrentMatch(userId);
    }

    @Override
    public void startDrivingToDestination(int userId, Location destination) {
        tripContext.startDrivingToDestination(userId, destination);
    }

    @Override
    public void arrive(int userId) {
        tripContext.arrive(userId);
    }

    @Override
    public TripView getCurrentTrip(int userId) {
        return tripContext.getCurrentTrip(userId);
    }

    @Override
    public TripView getTrip(String tripId) {
        return tripContext.getTrip(tripId);
    }
}
