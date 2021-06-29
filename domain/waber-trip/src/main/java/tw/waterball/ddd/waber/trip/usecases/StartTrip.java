package tw.waterball.ddd.waber.trip.usecases;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import tw.waterball.ddd.api.match.MatchContext;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.waber.trip.repositories.TripRepository;

import javax.inject.Named;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
@AllArgsConstructor
public class StartTrip {
    private final MatchContext matchContext;
    private final TripRepository tripRepository;

    @WithSpan
    public void execute(Request req, TripPresenter presenter) {
        MatchView matchView = matchContext.getMatch(req.matchId);
        Match match = matchView.toEntity();
        Trip trip = new Trip(match.getId());
        Trip savedTrip = tripRepository.saveTripWithMatch(trip, match);

        presenter.present(match, savedTrip);
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId;
        public int matchId;
    }
}
