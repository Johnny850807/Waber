package tw.waterball.ddd.waber.match.usecases;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tw.waterball.ddd.model.Jobs;
import tw.waterball.ddd.model.associations.Many;
import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;

import java.util.concurrent.TimeUnit;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@AllArgsConstructor
public class MatchingUseCase {
    private final Logger logger = LogManager.getLogger();
    private final Jobs matchingJobs;
    private final MatchRepository matchRepository;
    private final DistanceCalculator distanceCalculator;
    private final long rescheduleDelayTimeInMs;

    public Match execute(StartMatchingRequest req) {
        Match match = req.passenger.startMatching(req.matchPreferences);
        Match savedMatch = matchRepository.save(match);
        scheduleMatchingJob(savedMatch, req.drivers);
        return savedMatch;
    }

    private void scheduleMatchingJob(Match match, Many<Driver> drivers) {
        matchingJobs.scheduleWithFixedDelay(match.getId(),
                () -> matchDriver(match, drivers), 0, rescheduleDelayTimeInMs, TimeUnit.MICROSECONDS);
    }

    private void matchDriver(Match match, Many<Driver> drivers) {
        syncByJobId(match.getId(), () -> {
            match.perform(drivers.iterator(), distanceCalculator);
            if (match.isCompleted()) {
                matchRepository.save(match);
                matchingJobs.cancelJob(match.getId());
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
        synchronized (matchingJobs.getJob(matchId)) {
            if (matchingJobs.containsJob(matchId)) {
                runnable.run();
            }
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
}