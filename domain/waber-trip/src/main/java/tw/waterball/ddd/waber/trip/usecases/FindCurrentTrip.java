package tw.waterball.ddd.waber.trip.usecases;

import static java.util.Optional.ofNullable;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import tw.waterball.ddd.api.match.MatchContext;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.waber.trip.repositories.TripRepository;

import javax.inject.Named;
import java.util.Optional;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
@AllArgsConstructor
public class FindCurrentTrip {
    private final MatchContext matchContext;
    private final TripRepository tripRepository;

    public DefaultTripPresenter executeAndGet(Request request) {
        DefaultTripPresenter tripPresenter = new DefaultTripPresenter();
        execute(request, tripPresenter);
        return tripPresenter;
    }

    @WithSpan
    public void execute(Request request, TripPresenter presenter) {
        Match match = getCurrentMatch(request.userId).orElseThrow(NotFoundException::new);
        var mayHaveTrip = tripRepository.findByMatchId(match.getId());
        Trip trip = request.startNewTripIfNotFound ?
                mayHaveTrip.orElseGet(() -> startNewTrip(match))
                : mayHaveTrip.orElseThrow(NotFoundException::new);
        presenter.present(match, trip);
    }

    private Trip startNewTrip(Match match) {
        Trip trip = new Trip(match.getId());
        return tripRepository.saveTripWithMatch(trip, match);
    }

    private Optional<Match> getCurrentMatch(int userId) {
        MatchView matchView = matchContext.getCurrentMatch(userId);
        return ofNullable(matchView).map(MatchView::toEntity);
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Request {
        public final int userId;
        public boolean startNewTripIfNotFound = false;
    }

    public interface Presenter {
        void setMatch(Match match);

        void setTrip(Trip trip);
    }

}
