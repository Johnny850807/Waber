package tw.waterball.chaos.core;

import static java.util.stream.Collectors.groupingBy;

import lombok.RequiredArgsConstructor;
import tw.waterball.chaos.api.Chaos;
import tw.waterball.chaos.api.ChaosEngine;
import tw.waterball.chaos.api.ChaosEngineListener;
import tw.waterball.chaos.api.ChaosMiskillingException;
import tw.waterball.chaos.api.FunValue;

import java.util.Collection;
import java.util.HashSet;

/**
 * Stable State = Successful Car-Hailing flow from matching, traveling to payment checked.
 *
 * @author Waterball (johnny850807@gmail.com)
 */
@RequiredArgsConstructor
public class ChaosEngineImpl implements ChaosEngine {
    private final Collection<ChaosEngineListener> listeners = new HashSet<>();
    private final Collection<Chaos> chaosCollection;
    private FunValue funValue;

    @Override
    public void start(FunValue funValue) {
        nameMustNotDuplicate(chaosCollection);
        chaosCollection.forEach(c -> c.execute(funValue));
        this.funValue = funValue;
    }

    private void nameMustNotDuplicate(Collection<Chaos> chaosCollection) {
        chaosCollection.stream()
                .collect(groupingBy(Chaos::getName))
                .entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .findFirst()
                .ifPresent(e -> {
                    throw new IllegalArgumentException("Duplicate chaos names found: " + e.getKey() + ".");
                });
    }

    @Override
    public void addChaos(Chaos chaos) {
        nameMustNotDuplicate(chaos);
        chaosCollection.add(chaos);
        if (funValue != null) {
            chaos.execute(funValue);
        }
    }

    private void nameMustNotDuplicate(Chaos chaos) {
        if (chaosCollection.stream().anyMatch(c -> c.getName().equals(chaos.getName()))) {
            throw new IllegalArgumentException("Duplicate chaos names: " + chaos.getName() + ".");
        }
    }

    @Override
    public void removeChaosByName(String chaosName) {
        chaosCollection.removeIf(c -> c.getName().equals(chaosName));
    }

    @Override
    public void kill(String chaosName) {
        Chaos chaos = getChaos(chaosName)
                .filter(Chaos::isAlive)
                .orElseThrow(() -> new ChaosMiskillingException(chaosName));

        chaos.kill();
        listeners.forEach(l -> l.onChaosKilled(chaos));
        chaosCollection.remove(chaos);
    }

    @Override
    public void addListener(ChaosEngineListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ChaosEngineListener listener) {
        listeners.remove(listener);
    }


    @Override
    public Collection<Chaos> getChaosCollection() {
        return chaosCollection;
    }
}
