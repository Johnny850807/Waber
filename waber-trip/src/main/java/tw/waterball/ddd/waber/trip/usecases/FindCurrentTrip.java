package tw.waterball.ddd.waber.trip.usecases;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.waber.trip.repositories.TripRepository;

import javax.inject.Named;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
@AllArgsConstructor
public class FindCurrentTrip {
    private final MatchServiceDriver matchServiceDriver;
    private final TripRepository tripRepository;

    public DefaultTripPresenter executeAndGetResult(Request request) {
        DefaultTripPresenter tripPresenter = new DefaultTripPresenter();
        execute(request, tripPresenter);
        return tripPresenter;
    }

    public void execute(Request request, TripPresenter presenter) {
        Match match = getCurrentMatch(request.userId);
        var mayHaveTrip = tripRepository.findByMatchId(match.getId());
        Trip trip = request.startNewTripIfNotFound ? mayHaveTrip.orElseGet(() -> startNewTrip(match))
                :mayHaveTrip.orElseThrow(NotFoundException::new);
        presenter.present(match, trip);
    }

    private Trip startNewTrip(Match match) {
        Trip trip = new Trip(match.getId());
        return tripRepository.saveTripWithMatch(trip, match);
    }

    private Match getCurrentMatch(int userId) {
        MatchView matchView = matchServiceDriver.getCurrentMatch(userId);
        return matchView.toEntity();
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
