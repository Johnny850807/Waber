package tw.waterball.ddd.commons.utils;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class SneakyUtils {

    @FunctionalInterface
    public interface Supplier<T> {
        T get() throws Exception;
    }

    public static <T> T sneakyThrows(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
