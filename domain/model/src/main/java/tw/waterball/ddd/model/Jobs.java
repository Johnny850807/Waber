package tw.waterball.ddd.model;

import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Jobs {
    private Map<Integer, Job> idToJobMap = new HashMap<>();
    private ScheduledExecutorService scheduler;

    public Jobs(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }


    public Job<?> schedule(int id, Runnable runnable, long l, TimeUnit timeUnit) {
        Job job = new Job<>(scheduler.schedule(runnable, l, timeUnit));
        idToJobMap.put(id, job);
        return job;
    }

    public <V> Job<V> schedule(int id, Callable<V> callable, long l, TimeUnit timeUnit) {
        Job<V> job =  new Job<>(scheduler.schedule(callable, l, timeUnit));
        idToJobMap.put(id, job);
        return job;
    }

    public Job<?> scheduleAtFixedRate(int id, Runnable runnable, long l, long l1, TimeUnit timeUnit) {
        Job job = new Job<>(scheduler.scheduleAtFixedRate(runnable, l, l1, timeUnit));
        idToJobMap.put(id, job);
        return job;
    }

    public Job<?> scheduleWithFixedDelay(int id, Runnable runnable, long l, long l1, TimeUnit timeUnit) {
        Job job = new Job<>(scheduler.scheduleWithFixedDelay(runnable, l, l1, timeUnit));
        idToJobMap.put(id, job);
        return job;
    }

    public Job<?> schedule(Runnable runnable, long l, TimeUnit timeUnit) {
        return new Job<>(scheduler.schedule(runnable, l, timeUnit));
    }

    public <V> Job<V> schedule(Callable<V> callable, long l, TimeUnit timeUnit) {
        return new Job<>(scheduler.schedule(callable, l, timeUnit));
    }

    public Job<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit timeUnit) {
        return new Job<>(scheduler.scheduleAtFixedRate(runnable, initialDelay, period, timeUnit));
    }

    public Job<?> scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay, TimeUnit timeUnit) {
        return new Job<>(scheduler.scheduleWithFixedDelay(runnable, initialDelay, delay, timeUnit));
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return scheduler.shutdownNow();
    }

    public boolean isShutdown() {
        return scheduler.isShutdown();
    }

    public boolean isTerminated() {
        return scheduler.isTerminated();
    }

    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        return scheduler.awaitTermination(l, timeUnit);
    }

    public boolean containsJob(int jobId) {
        return idToJobMap.containsKey(jobId);
    }

    public Job getJob(int jobId) {
        return idToJobMap.get(jobId);
    }

    public void cancelJob(int jobId) {
        Job job = idToJobMap.remove(jobId);
        if (job == null) {
            throw new IllegalArgumentException(format("Cannot find the job (id=%d).", jobId));
        }
        job.cancel(true);
    }
}
