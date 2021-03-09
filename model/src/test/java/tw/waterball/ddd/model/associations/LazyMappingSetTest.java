package tw.waterball.ddd.model.associations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertTrue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

class LazyMappingSetTest {
    private Collection<String> lazyMappingSet;


    @BeforeEach
    void setup() {
        lazyMappingSet = new LazyMappingSet<>(
                new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4 ,5, 6, 7, 8, 9, 10)), String::valueOf
        );
    }

    @Test
    void testAdd() {
        lazyMappingSet.add("11");
        lazyMappingSet.add("12");
        lazyMappingSet.add("13");

        lazyMappingSet.removeIf(s -> Integer.parseInt(s) < 11);

        var iterator = lazyMappingSet.iterator();
        assertEquals("11", iterator.next());
        assertEquals("12", iterator.next());
        assertEquals("13", iterator.next());

    }

    @Test
    void testIterate() {
        lazyMappingSet.add("11");

        var iterator = lazyMappingSet.iterator();
        for (int i = 1; i <= 11; i++) {
            String next = iterator.next();
            assertEquals(i, Integer.parseInt(next));
        }
    }

    @Test
    void testContains() {
        assertTrue(lazyMappingSet.contains("1"));
        assertFalse(lazyMappingSet.contains("-1"));

        lazyMappingSet.removeAll(lazyMappingSet);
        assertFalse(lazyMappingSet.contains("1"));
        assertTrue(lazyMappingSet.isEmpty());
    }

}