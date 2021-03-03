package tw.waterball.ddd.waber.springboot.match.presenters;

import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.waber.match.usecases.MatchUseCase;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class MatchPresenter implements MatchUseCase.Presenter {
    private MatchView matchView;

    @Override
    public void present(Match match) {
        matchView = MatchView.toViewModel(match);
    }

    public MatchView getMatchView() {
        return matchView;
    }
}
