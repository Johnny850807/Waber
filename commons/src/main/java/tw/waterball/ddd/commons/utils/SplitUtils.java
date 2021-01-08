package tw.waterball.ddd.commons.utils;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class SplitUtils {
    public static <T, R> String splitByComma(Collection<T> collection, Function<T, R> conversion) {
        return collection.stream()
                .map(conversion)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
    public static <T> String splitByComma(Collection<T> collection) {
        return collection.stream().map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
