package tw.waterball.ddd.waber.springboot.user.tasks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tw.waterball.ddd.waber.springboot.user.repositories.jpa.ActivityData;
import tw.waterball.ddd.waber.springboot.user.repositories.jpa.JpaActivityDataPort;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
public class PopulateActivities {
    private JpaActivityDataPort dataPort;
    private List<String> activityNames;

    public PopulateActivities(JpaActivityDataPort dataPort,
                              @Value("${waber.activities}") List<String> activityNames) {
        this.dataPort = dataPort;
        this.activityNames = activityNames;
    }

    @PostConstruct
    public void execute() {
        dataPort.saveAll(activityNames.stream()
                .map(ActivityData::new)
                .collect(Collectors.toList()));
    }
}
