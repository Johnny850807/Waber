package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tw.waterball.ddd.model.associations.Many;
import tw.waterball.ddd.model.user.Activity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Entity @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
                        .map(UserData::fromEntity).collect(Collectors.toSet()));
    }

    public static Activity toEntity(ActivityData data) {
        Activity activity = new Activity(data.getId(), data.getName());
        activity.setParticipantDrivers(
                Many.lazyOn(() ->
                        data.getParticipantDrivers().stream()
                                .map(UserData::toDriver)
                                .collect(Collectors.toSet())
                )
        );
        return activity;
    }
}
