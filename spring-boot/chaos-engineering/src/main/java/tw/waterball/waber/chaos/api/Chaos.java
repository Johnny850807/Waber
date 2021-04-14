package tw.waterball.waber.chaos.api;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface Chaos {
    String getName();

    void execute(FunValue funValue);

    void kill();

    boolean isExecuted();

    boolean isAlive();

    boolean isKilled();
}
