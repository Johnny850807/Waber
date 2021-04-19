package tw.waterball.ddd.robots.life;

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
import tw.waterball.ddd.robots.api.API;
import tw.waterball.ddd.robots.api.StompAPI;

import java.util.Optional;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
public class PassengerBot extends AbstractUserBot {
    private final String name;
    private final StompAPI stompAPI;
    private final API api;
    private State state = State.NEWBORN;
    private Passenger passenger;
    private MatchView currentMatch;

    public PassengerBot(String name, StompAPI stompAPI, API api) {
        this.name = name;
        this.stompAPI = stompAPI;
        this.api = api;
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
        stompAPI.subscribe(new StompAPI.MatchedSubscription(passenger.getId()),
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
        stompAPI.subscribe(new StompAPI.TripStateChangedSubscription(passenger.getId()),
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
        stompAPI.subscribe(new StompAPI.UserLocationChangedSubscription(currentMatch.driver.id),
                (event, subscription) -> {
                    // only update while passenger is in the car
                    if (state == DRIVING_TO_DESTINATION) {
                        UserLocationUpdatedEvent theEvent = ((UserLocationUpdatedEvent) event);
                        if (theEvent.getUserId() == currentMatch.driver.id) {
                            // since the passenger is in the car, he's moving with the driver.
                            passenger.setLocation(theEvent.getLocation());
                            log.debug("<{}> Moved to {}", name, passenger.getLocation());
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

    @Override
    public Optional<Location> getLocation() {
        return Optional.ofNullable(passenger)
                .map(Passenger::getLocation);
    }
}
