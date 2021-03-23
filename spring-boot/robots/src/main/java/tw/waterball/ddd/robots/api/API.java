package tw.waterball.ddd.robots.api;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.api.trip.TripServiceDriver;
import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverIsNotAvailableException;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.api.payment.UserServiceDriver;

import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class API implements UserServiceDriver, MatchServiceDriver, TripServiceDriver {
    private final UserServiceDriver userServiceDriver;
    private final MatchServiceDriver matchServiceDriver;
    private final TripServiceDriver tripServiceDriver;

    @Override
    public void uploadLocation(int userId, Location location) {
        userServiceDriver.uploadLocation(userId, location);
    }

    @Override
    public Driver signUpAsDriver(String name, String email, String password, Driver.CarType carType) {
        return userServiceDriver.signUpAsDriver(name, email, password, carType);
    }

    @Override
    public Passenger signUpAsPassenger(String name, String email, String password) {
        return userServiceDriver.signUpAsPassenger(name, email, password);
    }

    @Override
    public Driver getDriver(int driverId) {
        return userServiceDriver.getDriver(driverId);
    }

    @Override
    public void setDriverStatus(int driverId, Driver.Status status) throws DriverIsNotAvailableException {
        userServiceDriver.setDriverStatus(driverId, status);
    }

    @Override
    public Passenger getPassenger(int passengerId) {
        return userServiceDriver.getPassenger(passengerId);
    }

    @Override
    public List<Driver> filterDrivers(MatchPreferences matchPreferences) {
        return userServiceDriver.filterDrivers(matchPreferences);
    }

    @Override
    public User getUser(int userId) {
        return userServiceDriver.getUser(userId);
    }

    @Override
    public MatchView startMatching(int userId, MatchPreferences matchPreferences) {
        return matchServiceDriver.startMatching(userId, matchPreferences);
    }

    @Override
    public MatchView getMatch(int matchId) {
        return matchServiceDriver.getMatch(matchId);
    }

    @Override
    public MatchView getCurrentMatch(int userId) {
        return matchServiceDriver.getCurrentMatch(userId);
    }

    @Override
    public void startDrivingToDestination(int userId, Location destination) {
        tripServiceDriver.startDrivingToDestination(userId, destination);
    }

    @Override
    public void arrive(int userId) {
        tripServiceDriver.arrive(userId);
    }

    @Override
    public TripView getTrip(String tripId) {
        return tripServiceDriver.getTrip(tripId);
    }
}
