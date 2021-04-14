package tw.waterball.waber.chaos.api;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface ChaosEngine {
    void start(FunValue funValue);

    void addChaos(Chaos chaos);

    void removeChaosByName(String chaosName);

    void kill(String chaosName);

    void addListener(Listener listener);

    void removeListener(Listener listener);

    default Optional<Chaos> getChaos(String chaosName) {
        return getChaosCollection().stream().filter(c -> c.getName().equals(chaosName.trim()))
                .findFirst();
    }

    Collection<Chaos> getChaosCollection();

    default Collection<Chaos> getAliveChaos() {
        return getChaosCollection().stream().filter(Chaos::isAlive).collect(toSet());
    }

    interface Listener {
        void onChaosKilled(Chaos chaos);
    }
}
