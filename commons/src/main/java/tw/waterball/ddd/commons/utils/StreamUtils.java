package tw.waterball.ddd.commons.utils;

import java.util.Iterator;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class StreamUtils {
    public static <T> Stream<T> iterate(Iterator<T> iterator) {
        return Stream.iterate(iterator, Iterator::hasNext, UnaryOperator.identity())
                .map(Iterator::next);
    }
}
