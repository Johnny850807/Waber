package tw.waterball.ddd.waber.springboot.match.repositories.jpa;

import lombok.*;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.user.Driver;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Table(name = "`match`") // 'match' is a reserved keyword in mysql and should be quoted
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchData {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private int passengerId;
    private Integer driverId;
    @Embedded
    private MatchPreferencesData matchPreferences;
    private Date createdDate;

    public static MatchData fromEntity(Match match) {
        return MatchData.builder()
                .id(match.getId())
                .driverId(match.getDriverOptional()
                        .map(Driver::getId).orElse(null))
                .passengerId(match.getPassengerId())
                .matchPreferences(MatchPreferencesData.fromEntity(match.getPreferences()))
                .createdDate(match.getCreatedDate())
                .build();
    }

    public Match toEntity() {
        return new Match(getId(), getPassengerId(),
                getDriverId(), getMatchPreferences().toEntity(),
                createdDate);
    }
}
