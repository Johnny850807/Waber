package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tw.waterball.ddd.model.user.Activity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;
import static tw.waterball.ddd.model.associations.LazyMappingSet.lazyMap;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Table(name = "activity")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityData {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String name;

    @ManyToMany
    public Set<UserData> participantDrivers = new HashSet<>();

    public ActivityData(String name) {
        this.name = name;
    }

    public static ActivityData fromEntity(Activity activity) {
        return new ActivityData(activity.getId(), activity.getName(),
                activity.getParticipantDrivers().stream()
                        .map(UserData::toData).collect(Collectors.toSet()));
    }

    public static Activity toEntity(ActivityData data) {
        return new Activity(data.getId(), data.getName(),
                lazyMap(data.getParticipantDrivers(), UserData::toDriver));
    }
}
