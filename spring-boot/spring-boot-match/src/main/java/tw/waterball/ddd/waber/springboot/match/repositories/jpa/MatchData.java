package tw.waterball.ddd.waber.springboot.match.repositories.jpa;

import lombok.*;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.user.Driver;

import javax.persistence.*;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Entity
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class MatchData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private int passengerId;
    private Integer driverId;
    private String driverName;
    @Embedded
    private MatchPreferencesData matchPreferences;

    public static MatchData fromEntity(Match match) {
            return MatchData.builder()
                    .id(match.getId())
                    .driverId(match.getDriverOptional()
                            .map(Driver::getId).orElse(null))
                    .driverName(match.getDriverOptional().map(Driver::getName).orElse(null))
                    .passengerId(match.getPassenger().getId())
                    .matchPreferences(MatchPreferencesData.fromEntity(match.getPreferences()))
                    .build();
    }

    public Match toEntity() {
        return new Match(getId(), getPassengerId(), getDriverId(), getMatchPreferences().toEntity());
    }
}
