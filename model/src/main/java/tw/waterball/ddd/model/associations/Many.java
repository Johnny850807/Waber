package tw.waterball.ddd.model.associations;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Many<T> implements Collection<T>, Association<Collection<T>> {
    private Collection<T> instances;
    private Supplier<Collection<T>> lazyInitializer;

    public Many() {
        this(Collections::emptySet);
    }

    public Many(Supplier<Collection<T>> lazyInitializer) {
        this.lazyInitializer = lazyInitializer;
    }

    public static <T> Many<T> lazyOn(Supplier<Collection<T>> lazyInitializer) {
        return new Many<>(lazyInitializer);
    }

    @Override
    public void resolveAssociation(Collection<T> instances) {
        this.instances = instances;
    }

    public Collection<T> get() {
        if (instances == null) {
            instances = lazyInitializer.get();
        }

        return instances;
    }

    public void reset() {
        instances = null;
    }

    @Override
    public boolean add(T t) {
        return get().add(t);
    }

    @Override
    public boolean remove(Object o) {
        return get().remove(o);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return get().addAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return get().removeAll(collection);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return get().removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return get().retainAll(collection);
    }

    @Override
    public void clear() {
        get().clear();
    }

    @Override
    public int size() {
        return get().size();
    }

    @Override
    public boolean isEmpty() {
        return get().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return get().contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return get().iterator();
    }

    @Override
    public Object[] toArray() {
        return get().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return get().toArray(t1s);
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        return get().toArray(generator);
    }


    @Override
    public boolean containsAll(Collection<?> collection) {
        return get().containsAll(collection);
    }

    @Override
    public boolean equals(Object o) {
        return get().equals(o);
    }

    @Override
    public int hashCode() {
        return get().hashCode();
    }

    @Override
    public Spliterator<T> spliterator() {
        return get().spliterator();
    }

    @Override
    public Stream<T> stream() {
        return get().stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return get().parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        get().forEach(action);
    }

}
