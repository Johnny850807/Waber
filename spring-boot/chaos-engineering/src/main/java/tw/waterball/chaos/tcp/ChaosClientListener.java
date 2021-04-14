package tw.waterball.chaos.tcp;

import tw.waterball.chaos.api.Chaos;
import tw.waterball.chaos.api.FunValue;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface ChaosClientListener {
    void onFunValueInitialized(FunValue funValue);

    void onChaosKilled(Chaos chaos);
}
