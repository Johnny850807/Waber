package tw.waterball.ddd.waber.match.usecases;

import static tw.waterball.ddd.commons.utils.DelayUtils.delay;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.MatchCompleteEvent;
import tw.waterball.ddd.events.StartMatchingCommand;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverIsNotAvailableException;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.api.payment.UserServiceDriver;
import tw.waterball.ddd.waber.match.domain.PerformMatch;
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
    private final PerformMatch performMatch;
    private final long rescheduleDelayTimeInMs;
    private final EventBus eventBus;

    @WithSpan
    public void execute(StartMatchingRequest req, Presenter presenter) {
        Passenger passenger = userServiceDriver.getPassenger(req.passengerId);
        Match match = Match.start(passenger.getId(), req.matchPreferences);
        matchRepository.save(match);
        eventBus.publish(new StartMatchingCommand(match.getId(), passenger.getId()));
        presenter.present(match);
    }

    @WithSpan
    public void execute(MatchRequest req) {
        Match match = matchRepository.findById(req.matchId).orElseThrow(NotFoundException::new);
        List<Driver> drivers = userServiceDriver.filterDrivers(match.getPreferences());

        performMatch.execute(match, drivers);

        if (match.isCompleted()) {
            try {
                saveIfMatchedDriverIsAvailable(match);
            } catch (DriverIsNotAvailableException err) {
                delayAndReplublishStartMatchingCommand(match);
            }
        } else {
            delayAndReplublishStartMatchingCommand(match);
        }
    }

    private void saveIfMatchedDriverIsAvailable(Match match) {
        userServiceDriver.setDriverStatus(match.getDriverId(), Driver.Status.MATCHED);
        log.info("<Match completed> passengerId: {}, driverId: {}", match.getPassengerId(), match.getDriverId());
        matchRepository.save(match);
        eventBus.publish(new MatchCompleteEvent(match));
    }

    private void delayAndReplublishStartMatchingCommand(Match match) {
        log.info("<Match Rescheduled> passengerId: {}", match.getPassengerId());
        delay(rescheduleDelayTimeInMs);
        eventBus.publish(new StartMatchingCommand(match.getId(), match.getPassengerId()));
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
