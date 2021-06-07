package tw.waterball.ddd.commons.utils;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class MapUtils {
    public static <K, V> Map<K, V> zip(K[] keys, V[] values) {
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    @SafeVarargs
    public static <K> MapBuilder<K> map(K... keys) {
        return new MapBuilder<>(keys);
    }

    @AllArgsConstructor
    public static class MapBuilder<K> {
        private final K[] keys;

        @SafeVarargs
        public final <V> Map<K, V> to(V... values) {
            return zip(keys, values);
        }

    }
}
