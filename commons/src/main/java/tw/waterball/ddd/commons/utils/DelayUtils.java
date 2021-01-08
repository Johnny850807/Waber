package tw.waterball.ddd.commons.utils;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class DelayUtils {

    public static void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
