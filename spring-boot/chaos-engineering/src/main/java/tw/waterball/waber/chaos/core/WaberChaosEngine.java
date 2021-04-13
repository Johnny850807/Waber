package tw.waterball.waber.chaos.core;

import lombok.AllArgsConstructor;
import tw.waterball.waber.chaos.api.ChaosEngine;
import tw.waterball.waber.chaos.api.FunValue;

import java.util.Collection;

/**
 * Stable State = Successful Car-Hailing flow from matching, traveling to payment checked.
 *
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class WaberChaosEngine implements ChaosEngine {
    private final Collection<WaberChaos> waberChaosCollection;

    @Override
    public void start(FunValue funValue) {
        waberChaosCollection.forEach(c -> c.execute(funValue));
    }
}
