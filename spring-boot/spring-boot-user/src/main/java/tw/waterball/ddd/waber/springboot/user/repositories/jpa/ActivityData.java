package tw.waterball.ddd.waber.springboot.match.repositories.jpa;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.match.Activity;
import tw.waterball.ddd.model.user.Driver;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ActivityData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Integer id;
    public String name;
    public Set<Integer> participantDriverIds = new HashSet<>();

    public static ActivityData fromEntity(Activity activity) {
        return new ActivityData(activity.getId(), activity.getName(),
                activity.getParticipantDrivers().stream()
                        .map(Driver::getId).collect(Collectors.toSet()));
    }

    public static Activity toEntity(ActivityData data) {
        Activity activity = new Activity(data.id, data.name);
        data.participantDriverIds.stream()
                .map(Driver::new)
                .forEach(activity::participate);
        return activity;
    }
}
