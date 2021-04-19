package tw.waterball.ddd.waber.springboot.user.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tw.waterball.ddd.waber.springboot.user.repositories.jpa.ActivityData;
import tw.waterball.ddd.waber.springboot.user.repositories.jpa.ActivityDAO;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Slf4j
@Component
public class PopulateActivities {
    private final ActivityDAO dataPort;
    private final List<String> activityNames;

    public PopulateActivities(ActivityDAO dataPort,
                              @Value("${waber.activities}") List<String> activityNames) {
        this.dataPort = dataPort;
        this.activityNames = activityNames;
    }

    @PostConstruct
    public void execute() {
        log.info("Save activities: {}", String.join(", ", activityNames));
        dataPort.saveAll(activityNames.stream()
                .map(ActivityData::new)
                .collect(Collectors.toList()));
    }
}
