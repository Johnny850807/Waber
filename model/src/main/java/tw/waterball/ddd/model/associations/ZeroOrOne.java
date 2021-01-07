package tw.waterball.ddd.model.associations;

import tw.waterball.ddd.model.base.Entity;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class ZeroOrOne<T extends Entity> implements Association<T> {
    private T value;
    private Object id;
    private Supplier<T> lazyInitializer = () -> null;

    public ZeroOrOne() {
    }

    public ZeroOrOne(T value) {
        this.value = value;
    }

    public ZeroOrOne(Supplier<T> lazyInitializer) {
        this.lazyInitializer = lazyInitializer;
    }

    public static <T extends Entity> ZeroOrOne<T> lazyOn(Supplier<T> lazyInitializer) {
        return new ZeroOrOne<>(lazyInitializer);
    }

    public void reset() {
        value = null;
        id = null;
    }

    @Override
    public void resolveAssociation(T value) {
        this.value = value;
        this.id = value == null ? null : value.getId();
    }

    public void resolveId(Object id) {
        if (value != null) {
            throw new IllegalStateException("Cannot resolve the id after thr value has been resolved.");
        }
        this.id = id;
    }

    @SuppressWarnings("unchecked")
    public <ID> Optional<ID> getId() {
        return Optional.ofNullable((ID) id);
    }

    public boolean exists() {
        return id != null;
    }

    public Optional<T> get() {
        if (value == null) {
            value = lazyInitializer.get();
            if (id != null && value == null) {
                throw new IllegalStateException("The association is solved with an id, but the initializer is not found.");
            }
            if (value != null) {
                id = value.getId();
            }
        }
        return Optional.ofNullable(value);
    }

}
