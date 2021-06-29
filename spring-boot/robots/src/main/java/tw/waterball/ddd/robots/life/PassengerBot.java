package tw.waterball.ddd.robots.life;

import static java.util.Optional.ofNullable;
import static tw.waterball.ddd.robots.life.PassengerBot.State.ACTIVE;
import static tw.waterball.ddd.robots.life.PassengerBot.State.DRIVING_TO_DESTINATION;
import static tw.waterball.ddd.robots.life.PassengerBot.State.MATCHING;

import lombok.extern.slf4j.Slf4j;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.events.UserLocationUpdatedEvent;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.trip.TripStateType;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.robots.Framework;
import tw.waterball.ddd.robots.api.API;
import tw.waterball.ddd.robots.api.BrokerAPI;

import java.util.Optional;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
public class PassengerBot extends AbstractUserBot {
    private final BrokerAPI brokerAPI;
    private final API api;
    private Framework framework;
    private State state = State.NEWBORN;
    private Passenger passenger;
    private MatchView currentMatch;

    public PassengerBot(String name, BrokerAPI brokerAPI, API api, Framework framework) {
        super(name);
        this.brokerAPI = brokerAPI;
        this.api = api;
        this.framework = framework;
    }

    public enum State {
        NEWBORN, ACTIVE, MATCHING, MATCHED, DRIVING_TO_DESTINATION
    }

    @Override
    public void onNewBorn() {
    }

    @Override
    public void onTick(long currentTime) {
        handleNewborn();
        handleActive();
        handleMatching();
        handleMatched();
    }

    private void handleNewborn() {
        if (state == State.NEWBORN) {
            if (passenger == null) {
                passenger = api.signUpAsPassenger(name, name + "@robot.io", name + "-password");
                passenger.setLocation(randomLocation());
                updateLocation();
            }
            subscribeToMatchedCompletionEvent();
            subscribeToTripStateChangedEvent();
            log.info("<{}> Signed up and set the location at {}", name, passenger.getLocation());
            state = State.ACTIVE;
        }
    }

    private void updateLocation() {
        api.uploadLocation(passenger.getId(), passenger.getLocation());
    }

    private void subscribeToMatchedCompletionEvent() {
        brokerAPI.subscribe(new BrokerAPI.MatchedSubscription(passenger.getId()),
                (event, subscription) -> {
                    if (state != State.MATCHING) {
                        throw new IllegalStateException("Bug, get MATCHED in the states other than the MATCHING state.");
                    }
                    currentMatch = api.getCurrentMatch(passenger.getId());
                    log.info("<{}> Matched to the driver({})", name, currentMatch.driver.name);
                    subscribeToDriverLocationUpdatedEvent();

                    state = State.MATCHED;
                });
    }


    private void subscribeToTripStateChangedEvent() {
        brokerAPI.subscribe(new BrokerAPI.TripStateChangedSubscription(passenger.getId()),
                (event, subscription) -> {
                    String content = (String) event;
                    TripStateType tripState = TripStateType.valueOf(content);
                    if (tripState == TripStateType.DRIVING) {
                        state = DRIVING_TO_DESTINATION;
                        log.info("<{}> Driving to destination", name);
                    } else if (tripState == TripStateType.ARRIVED) {
                        log.info("<{}> Arrived", name);
                        passenger.setLocation(randomLocation());  // re-born to another location
                        updateLocation();
                        state = ACTIVE;
                    }
                });
    }

    private void subscribeToDriverLocationUpdatedEvent() {
        brokerAPI.subscribe(new BrokerAPI.UserLocationChangedSubscription(currentMatch.driver.id),
                (event, subscription) -> {
                    // only update while passenger is in the car
                    if (state == DRIVING_TO_DESTINATION) {
                        UserLocationUpdatedEvent theEvent = ((UserLocationUpdatedEvent) event);
                        if (theEvent.getUserId() == currentMatch.driver.id) {
                        } else {
                            subscription.unsubscribe();
                        }
                    }
                });
    }

    private void handleActive() {
        if (state == State.ACTIVE) {
            if (random.nextInt(100) < 20) {  // 20% probability
                state = MATCHING;
                api.startMatching(passenger.getId(), randomMatchPreferences());
                log.info("<{}> Start matching {}...", passenger.getLocation(), name);
            }
        }
    }

    private MatchPreferences randomMatchPreferences() {
        return new MatchPreferences(passenger.getLocation(), randomCarType(), null);
    }

    private void handleMatching() {
        // do nothing
    }

    private void handleMatched() {
        // do nothing
    }

    public Optional<Integer> getPassengerId() {
        return Optional.of(passenger).map(Passenger::getId);
    }

    @Override
    public Optional<Location> getLocation() {
        return ofNullable(passenger)
                .map(Passenger::getLocation);
    }

    @Override
    public String getState() {
        switch (state) {
            case MATCHING:
                return "Matching...";
            case MATCHED:
                return "Waiting for pickup...";
            default:
                return "";
        }
    }

    public void setLocation(Location location) {
        if (passenger == null) {
            throw new IllegalStateException("Passenger is not ready, can't set its location.");
        }
        passenger.setLocation(location);
        log.debug("<{}> Moved to {}", name, passenger.getLocation());
    }
}
