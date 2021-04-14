package tw.waterball.ddd.model.associations;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class LazyMappingSet<T, R> implements Set<R> {
    private final Collection<T> set;
    private final Collection<R> mappingResult;
    private boolean hasMapped = false;
    private final Function<T, R> mapping;

    public static <T, R> LazyMappingSet<T, R> lazyMap(Set<T> set,
                                                      Function<T, R> mapping) {
        return new LazyMappingSet<>(set, mapping);
    }

    public LazyMappingSet(Collection<T> set, Function<T, R> mapping) {
        this.set = set;
        this.mapping = mapping;
        if (set instanceof LinkedHashSet) {
            mappingResult = new LinkedHashSet<>();
        } else if (set instanceof TreeSet) {
            mappingResult = new TreeSet<>();
        } else {
            mappingResult = new HashSet<>();
        }
    }


    @Override
    public int size() {
        return hasMapped ? mappingResult.size() : set.size();
    }

    @Override
    public boolean isEmpty() {
        return hasMapped ? mappingResult.isEmpty() : set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        lazyMap();
        return mappingResult.contains(o);
    }

    @Override
    public Iterator<R> iterator() {
        lazyMap();
        return mappingResult.iterator();
    }

    @Override
    public Object[] toArray() {
        lazyMap();
        return mappingResult.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        lazyMap();
        return mappingResult.toArray(a);
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        lazyMap();
        return mappingResult.toArray(generator);
    }

    @Override
    public boolean add(R r) {
        lazyMap();
        return mappingResult.add(r);
    }

    @Override
    public boolean remove(Object o) {
        lazyMap();
        return mappingResult.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lazyMap();
        return mappingResult.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends R> c) {
        lazyMap();
        return mappingResult.addAll(c);
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        lazyMap();
        return mappingResult.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super R> filter) {
        lazyMap();
        return mappingResult.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lazyMap();
        return mappingResult.retainAll(c);
    }

    @Override
    public void clear() {
        lazyMap();
        mappingResult.clear();
    }

    @Override
    public boolean equals(Object o) {
        lazyMap();
        return mappingResult.equals(o);
    }

    @Override
    public int hashCode() {
        lazyMap();
        return mappingResult.hashCode();
    }

    private void lazyMap() {
        if (!hasMapped) {
            for (T t : set) {
                mappingResult.add(mapping.apply(t));
            }
            hasMapped = true;
        }
    }

}
