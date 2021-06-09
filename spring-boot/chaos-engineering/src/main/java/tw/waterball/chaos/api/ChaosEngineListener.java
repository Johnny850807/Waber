package tw.waterball.chaos.api;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface ChaosEngineListener {
    void onFunValueInitialized(FunValue funValue);
    void onChaosKilled(Chaos chaos);
}
