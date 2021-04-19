package tw.waterball.ddd.robots.life;

import lombok.extern.slf4j.Slf4j;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.robots.api.API;
import tw.waterball.ddd.robots.api.StompAPI;

import java.util.Optional;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
public class DriverBot extends AbstractUserBot {
    public static final double SPEED = 8;
    private final String name;
    private Driver driver;
    private final StompAPI stompAPI;
    private final API api;
    private State state = State.NEWBORN;
    private MatchView currentMatch;
    private Location destination;

    private enum State {
        NEWBORN, ACTIVE, PICKING_UP_PASSENGER, DRIVING_TO_DESTINATION
    }

    public DriverBot(String name, StompAPI stompAPI, API api) {
        this.name = name;
        this.stompAPI = stompAPI;
        this.api = api;

    }

    @Override
    protected void onNewBorn() {
    }

    @Override
    public void onTick(long currentTime) {
        handleNewborn();
        handleActive();
        handlePickingUpPassenger();
        handleDrivingToDestination();
    }

    private void handleNewborn() {
        if (state == State.NEWBORN) {
            if (driver == null) {
                driver = api.signUpAsDriver(name, name + "@robot.io", name + "-password", randomCarType());
                driver.setLocation(randomLocation());
                updateLocation();
            }
            log.info("<{}> Signed up and set the location with {}", name, driver.getLocation());

            subscribeToMatchedCompletionEvent();
            state = State.ACTIVE;
        }
    }

    private void subscribeToMatchedCompletionEvent() {
        stompAPI.subscribe(new StompAPI.MatchedSubscription(driver.getId()),
                (event, subscription) -> {
                    if (state == State.ACTIVE) {
                        currentMatch = api.getCurrentMatch(driver.getId());
                        log.info("<{}> Matched to a passenger (id={}).", name, currentMatch.passengerId);
                        state = State.PICKING_UP_PASSENGER;
                    }
                });
    }


    private void handleActive() {
        // do nothing
    }

    private void handlePickingUpPassenger() {
        if (state == State.PICKING_UP_PASSENGER) {
            Location startLocation = currentMatch.matchPreferences.getStartLocation();
            ifArriveOtherwiseMoveToward(startLocation, () -> {
                destination = generateDestination();
                api.startDrivingToDestination(driver.getId(), destination);
                state = State.DRIVING_TO_DESTINATION;
                log.info("<{}> Start driving to the destination {}", name, destination);

            });
        }
    }

    private void handleDrivingToDestination() {
        if (state == State.DRIVING_TO_DESTINATION) {
            ifArriveOtherwiseMoveToward(destination, () -> {
                api.arrive(driver.getId());
                destination = null;
                currentMatch = null;
                state = State.ACTIVE;
                log.info("<{}> Arrived", name);
            });
        }
    }

    private void ifArriveOtherwiseMoveToward(Location goal, Runnable doIfArrive) {
        if (driver.getLocation().distance(goal) <= SPEED * 2) {
            doIfArrive.run();
        } else {
            Location beforeMove = new Location(driver.getLocation().getLatitude(), driver.getLocation().getLongitude());
            driver.getLocation().moveToward(goal, SPEED);
            updateLocation();
            log.debug("<{}> Moving {} --> {} toward the destination ({}). (Remaining distance: {})", name,
                    goal, beforeMove, driver.getLocation(), driver.getLocation().distance(goal));
        }
    }

    private void updateLocation() {
        if (driver.getLocation().getLatitude() == 0 && driver.getLocation().getLongitude() == 0) {
            System.out.println("Error");
        }
        api.uploadLocation(driver.getId(), driver.getLocation());
    }

    private Location generateDestination() {
        return randomLocation();
    }

    @Override
    public Optional<Location> getLocation() {
        return Optional.ofNullable(driver)
                .map(Driver::getLocation);
    }

    public Optional<Location> getDestination() {
        return Optional.ofNullable(destination);
    }

    public Driver.Status getStatus() {
        return state == State.ACTIVE ? Driver.Status.AVAILABLE : Driver.Status.MATCHED;
    }
}
