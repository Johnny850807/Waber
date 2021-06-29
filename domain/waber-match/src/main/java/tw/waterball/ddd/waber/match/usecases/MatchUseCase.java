package tw.waterball.ddd.waber.match.usecases;

import static tw.waterball.ddd.commons.utils.DelayUtils.delay;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.currentSpan;
import static tw.waterball.ddd.model.user.Driver.Status.MATCHED;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.MatchCompleteEvent;
import tw.waterball.ddd.events.StartMatchingCommand;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverNotAvailableException;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.api.payment.UserContext;
import tw.waterball.ddd.waber.match.domain.CarHailingMatcher;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;

import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Slf4j
@AllArgsConstructor
public class MatchUseCase {
    private final UserContext userContext;
    private final MatchRepository matchRepository;
    private final FindCurrentMatch findCurrentMatch;
    private final CarHailingMatcher carHailingMatcher;
    private final long rescheduleDelayTimeInMs;
    private final EventBus eventBus;

    public void execute(StartMatchingRequest request, Presenter presenter) {
        Passenger passenger = userContext.getPassenger(request.passengerId);
        Match match = findCurrentMatch.execute(request.passengerId)
                .orElseGet(() -> startNewMatch(request, passenger));
        presenter.present(match);
    }

    private Match startNewMatch(StartMatchingRequest request, Passenger passenger) {
        Match newMatch = Match.start(passenger.getId(), request.matchPreferences);
        newMatch = matchRepository.save(newMatch);
        eventBus.publish(new StartMatchingCommand(newMatch.getId(), passenger.getId()));
        return newMatch;
    }

    public void execute(PerformMatchRequest request) {
        Match match = findMatch(request);
        List<Driver> drivers = userContext.getDrivers(match.getPreferences());

        carHailingMatcher.match(match, drivers);

        if (match.isCompleted()) {
            try {
                saveIfMatchedDriverIsAvailable(match);
            } catch (DriverNotAvailableException err) {
                reschedule(match);
            }
        } else {
            reschedule(match);
        }
    }

    private Match findMatch(PerformMatchRequest req) {
        return matchRepository.findById(req.matchId).orElseThrow(NotFoundException::new);
    }

    private void saveIfMatchedDriverIsAvailable(Match match) throws DriverNotAvailableException {
        userContext.askDriverToMatch(match.getDriverId());
        match = matchRepository.save(match);
        eventBus.publish(new MatchCompleteEvent(match));
    }

    private void reschedule(Match match) {
        delay(rescheduleDelayTimeInMs);
        eventBus.publish(new StartMatchingCommand(match.getId(), match.getPassengerId()));
    }

    @AllArgsConstructor
    public static class StartMatchingRequest {
        public int passengerId;
        public MatchPreferences matchPreferences;
    }


    @AllArgsConstructor
    public static class PerformMatchRequest {
        public int matchId;
    }


    public interface Presenter {
        void present(Match match);
    }
}
