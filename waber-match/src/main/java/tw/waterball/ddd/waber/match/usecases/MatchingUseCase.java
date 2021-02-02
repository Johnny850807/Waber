package tw.waterball.ddd.waber.match.usecases;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.waterball.ddd.model.Jobs;
import tw.waterball.ddd.model.associations.Many;
import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverHasBeenMatchedException;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.api.payment.UserServiceDriver;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;

import java.util.concurrent.TimeUnit;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Slf4j
@AllArgsConstructor
public class MatchingUseCase {
    private final UserServiceDriver userServiceDriver;
    private final Jobs matchingJobs;
    private final MatchRepository matchRepository;
    private final DistanceCalculator distanceCalculator;
    private final long rescheduleDelayTimeInMs;


    public void execute(StartMatchingRequest req, Presenter presenter) {
        Match match = Match.start(req.passenger, req.matchPreferences);
        Match savedMatch = matchRepository.save(match);
        scheduleMatchingJob(savedMatch, req.drivers);
        presenter.present(match);
    }

    private void scheduleMatchingJob(Match match, Many<Driver> drivers) {
        matchingJobs.scheduleWithFixedDelay(match.getId(),
                () -> matchDriver(match, drivers), 0, rescheduleDelayTimeInMs, TimeUnit.MILLISECONDS);
    }

    private void matchDriver(Match match, Many<Driver> drivers) {
        syncByJobId(match.getId(), () -> {
            match.perform(drivers.iterator(), distanceCalculator);
            if (match.isCompleted()) {
                try {
                    userServiceDriver.setDriverStatus(match.getDriver().getId(), Driver.Status.MATCHED);
                    matchRepository.save(match);
                    matchingJobs.cancelJob(match.getId());
                    log.info("Matched Driver: {}.", match.getDriver().getName());
                } catch (DriverHasBeenMatchedException err) {
                    match.cancel();
                    log.info(err.getMessage());
                }
            } else {
                log.info("The match is still pending...");
            }

            drivers.reset();
        });
    }

    public void cancel(int matchId) {
        Match match = matchRepository.associateById(matchId);
        syncByJobId(matchId, () -> {
            match.cancel();
            matchRepository.save(match);
            matchingJobs.cancelJob(matchId);
        });
    }

    private void syncByJobId(int matchId, Runnable runnable) {
        try {
            if (matchingJobs.containsJob(matchId)) {
                synchronized (matchingJobs.getJob(matchId)) {
                    if (matchingJobs.containsJob(matchId)) {
                        runnable.run();
                    }
                }
            }
        } catch (Exception err) {
            log.error(err.getMessage(), err);
            matchingJobs.cancelJob(matchId);
        }
    }

    @AllArgsConstructor
    public static class StartMatchingRequest {
        public Passenger passenger;
        public Many<Driver> drivers;
        public MatchPreferences matchPreferences;
    }

    @AllArgsConstructor
    public static class CancelMatchingRequest {
        public Passenger passenger;
        public int matchId;
    }

    public interface Presenter {
        void present(Match match);
    }
}
