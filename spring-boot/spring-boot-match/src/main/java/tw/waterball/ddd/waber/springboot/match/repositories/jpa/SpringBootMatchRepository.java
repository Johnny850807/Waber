package tw.waterball.ddd.waber.springboot.match.repositories.jpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
@AllArgsConstructor
public class SpringBootMatchRepository implements MatchRepository {
    private final MatchDAO matchDAO;

    @Override
    public List<Match> saveAll(Match ...matches) {
        List<MatchData> data = matchDAO.saveAll(Arrays.stream(matches)
                .map(MatchData::fromEntity).collect(Collectors.toList()));
        for (int i = 0; i < data.size(); i++) {
            matches[i].setId(data.get(i).getId());
        }
        return Arrays.asList(matches);
    }

    @Override
    public Match save(Match match) {
        MatchData data = matchDAO.save(MatchData.fromEntity(match));
        match.setId(data.getId());
        return match;
    }

    @Override
    public Optional<Match> findById(int matchId) {
        return matchDAO.findById(matchId)
                .map(MatchData::toEntity);
    }

    @Override
    public Optional<Match> findPassengerCurrentMatch(int passengerId) {
        return matchDAO.findFirstByPassengerIdAndAliveIsTrueOrderByCreatedDateDesc(passengerId)
                .map(MatchData::toEntity);
    }

    @Override
    public Optional<Match> findDriverCurrentMatch(int driverId) {
        return matchDAO.findFirstByDriverIdAndAliveIsTrueOrderByCreatedDateDesc(driverId)
                .map(MatchData::toEntity);
    }

    @Override
    public Match associateById(int matchId) {
        try {
            return matchDAO.getOne(matchId).toEntity();
        } catch (EntityNotFoundException err) {
            throw new NotFoundException(err);
        }
    }

    @Override
    public void setAlive(int matchId, boolean alive) {
        matchDAO.setAlive(matchId, alive);
    }

    @Override
    public void removeAll() {
        matchDAO.deleteAll();
    }
}
