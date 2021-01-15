package tw.waterball.ddd.model.associations;

import tw.waterball.ddd.model.base.Entity;
import tw.waterball.ddd.model.trip.Trip;

import java.util.function.Supplier;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class One<T extends Entity> implements Association<T> {
    private T value;
    private Object id;
    private Supplier<T> lazyInitializer;

    public One() {
    }

    public One(T value) {
        this(value, null);
    }

    public One(T value, Supplier<T> lazyInitializer) {
        this.value = value;
        this.id = value.getId();
        this.lazyInitializer = lazyInitializer;
    }

    private One(Object id) {
        resolveId(id);
    }

    public One(Supplier<T> lazyInitializer) {
        this.lazyInitializer = lazyInitializer;
    }

    public static <T extends Entity> One<T> associate(Object id) {
        return new One<>(id);
    }

    public static <T extends Entity> One<T> lazyOn(Supplier<T> lazyInitializer) {
        return new One<>(lazyInitializer);
    }

    public static <T extends Entity> One<T> of(T t) {
        return new One<>(t);
    }

    public static One<Trip> empty() {
        return new One<>();
    }

    public void reset() {
        value = null;
        id = null;
    }

    @Override
    public void resolveAssociation(T value) {
        this.value = value;
        this.id = value.getId();
    }

    public void resolveId(Object id) {
        if (value != null) {
            throw new IllegalStateException("Cannot resolve the id after thr value has been resolved.");
        }
        this.id = id;
    }

    @SuppressWarnings("unchecked")
    public <ID> ID getId() {
        return (ID) id;
    }

    public boolean exists() {
        return id != null;
    }

    public T get() {
        if (value == null) {
            if (id != null && lazyInitializer == null) {
                throw new IllegalStateException("The association is solved with an id, but the initializer is not found.");
            }
            value = lazyInitializer.get();
        }
        if (value == null) {
            throw new AssociationViolationException("The instance of the One Association can't be resolved.");
        } else {
            id = value.getId();
        }
        return value;
    }
}
