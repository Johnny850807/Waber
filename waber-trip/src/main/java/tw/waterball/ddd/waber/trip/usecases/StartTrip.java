package tw.waterball.ddd.waber.trip.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.api.match.MatchServiceDriver;
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
    private MatchServiceDriver matchServiceDriver;
    private TripRepository tripRepository;

    public void execute(Request req, TripPresenter presenter) {
        MatchView matchView = matchServiceDriver.getMatch(req.passengerId, req.matchId);
        Match match = matchView.toEntity();
        Trip trip = new Trip(match);
        Trip savedTrip = tripRepository.save(trip);
        presenter.present(savedTrip);
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId;
        public int matchId;
    }
}
