package tw.waterball.waber.chaos.api;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class ChaosKillingFailException extends IllegalStateException {
    public ChaosKillingFailException(String targetName) {
        super("You intended to kill " + targetName + ", but it does not even exists!");
    }
}
