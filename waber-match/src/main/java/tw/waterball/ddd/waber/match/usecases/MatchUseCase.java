package tw.waterball.ddd.waber.match.usecases;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.MatchCompleteEvent;
import tw.waterball.ddd.events.StartMatchingCommand;
import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverHasBeenMatchedException;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.api.payment.UserServiceDriver;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;

import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Slf4j
@AllArgsConstructor
public class MatchUseCase {
    private final UserServiceDriver userServiceDriver;
    private final MatchRepository matchRepository;
    private final DistanceCalculator distanceCalculator;
    private final long rescheduleDelayTimeInMs;
    private final EventBus eventBus;

    public void execute(StartMatchingRequest req, Presenter presenter) {
        Passenger passenger = userServiceDriver.getPassenger(req.passengerId);
        Match match = Match.start(passenger, req.matchPreferences);
        matchRepository.save(match);
        eventBus.publish(new StartMatchingCommand(match.getId(), passenger.getId()));
        presenter.present(match);
    }

    @SneakyThrows
    public void execute(MatchRequest req) {
        Match match = matchRepository.findById(req.matchId)
                .orElseThrow(NotFoundException::new);
        List<Driver> drivers = userServiceDriver.filterDrivers(match.getPreferences());
        match.perform(drivers, distanceCalculator);
        if (match.isCompleted()) {
            try {
                userServiceDriver.setDriverStatus(match.getDriver().getId(), Driver.Status.MATCHED);
                matchRepository.save(match);
                eventBus.publish(new MatchCompleteEvent(match));
                log.info("Matched Driver: {}.", match.getDriver().getName());
            } catch (DriverHasBeenMatchedException err) {
                Thread.sleep(rescheduleDelayTimeInMs + (int)(Math.random() * 1000));
                eventBus.publish(new StartMatchingCommand(match.getId(), match.getPassengerId()));
                log.info(err.getMessage());
            }
        } else {
            log.info("The match is pending...");
        }
    }

    @AllArgsConstructor
    public static class StartMatchingRequest {
        public int passengerId;
        public MatchPreferences matchPreferences;
    }


    @AllArgsConstructor
    public static class MatchRequest {
        public int matchId;
    }


    public interface Presenter {
        void present(Match match);
    }
}
