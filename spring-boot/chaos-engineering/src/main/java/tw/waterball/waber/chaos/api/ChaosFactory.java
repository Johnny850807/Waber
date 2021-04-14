package tw.waterball.waber.chaos.api;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface ChaosFactory {
    FunValue createFunValue(String funValue);
    FunValuePacker createFunValuePacker();
}
