package tw.waterball.chaos.tcp;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface ChaosServerListener {
    void onChaosRegistered(String[] chaosNames);

    void onChaosClaimedAlive(String[] chaosNames);

}
