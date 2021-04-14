package tw.waterball.chaos.api;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class ChaosMiskillingException extends IllegalStateException {
    public ChaosMiskillingException(String targetName) {
        super("You are mis-killing " + targetName + ", he is not the mole!");
    }
}
