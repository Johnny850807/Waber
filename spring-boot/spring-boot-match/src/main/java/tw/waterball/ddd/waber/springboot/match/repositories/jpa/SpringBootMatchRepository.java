package tw.waterball.ddd.waber.springboot.match.repositories.jpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
@AllArgsConstructor
public class SpringBootMatchRepository implements MatchRepository {
    private JpaMatchDataPort dataPort;

    @Override
    public Match save(Match match) {
        MatchData data = dataPort.save(MatchData.fromEntity(match));
        match.setId(data.getId());
        return match;
    }

    @Override
    public Optional<Match> findById(int matchId) {
        return dataPort.findById(matchId)
                .map(MatchData::toEntity);
    }

    @Override
    public Match associateById(int matchId) {
        try {
            return dataPort.getOne(matchId).toEntity();
        } catch (EntityNotFoundException err) {
            throw new NotFoundException(err);
        }
    }
}
