package tw.waterball.ddd.model;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class ManyAssociation<T> {
    private Collection<T> result;
    private Supplier<Collection<T>> lazyInitialize;

    public ManyAssociation(Supplier<Collection<T>> lazyInitializer) {
        this.lazyInitialize = lazyInitializer;
    }

    public static <T> ManyAssociation lazyOn(Supplier<Collection<T>> lazyInitializer) {
        return new ManyAssociation<T>(lazyInitializer);
    }

    public Collection<T> get() {
        if (result == null) {
            result = lazyInitialize.get();
        }
        return result;
    }

}
