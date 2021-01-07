package tw.waterball.ddd.model;

import java.util.concurrent.*;

/**
 * Rename the ScheduledFuture.
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Job<V> implements ScheduledFuture<V> {
    private ScheduledFuture<V> delegate;

    public Job(ScheduledFuture<V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return delegate.getDelay(timeUnit);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return delegate.compareTo(delayed);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return delegate.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public boolean isDone() {
        return delegate.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return delegate.get();
    }

    @Override
    public V get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.get(l, timeUnit);
    }
}
