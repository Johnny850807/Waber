package tw.waterball.ddd.robots;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class Timeline {
    private long currentTime;
    private final Map<Long, Collection<Action>> actionsMap = new LinkedHashMap<>();

    public void tick(long currentTime) {
        this.currentTime = currentTime;

        actionsMap.getOrDefault(currentTime, emptyList())
                .parallelStream()
                .forEach(Action::perform);
    }

    public CancelSchedule scheduleFixed(long delay, Consumer<CancelSchedule> action) {
        return schedule(new FixedAction(delay, action));
    }

    public CancelSchedule schedule(long delay, Consumer<CancelSchedule> action) {
        return schedule(new Action(delay, action));
    }

    private CancelSchedule schedule(Action action) {
        if (action.delay <= 0) {
            throw new IllegalArgumentException("Delay must be positive.");
        }
        long scheduledTime = currentTime + action.delay;

        var actions = actionsMap.computeIfAbsent(scheduledTime, key -> new LinkedHashSet<>());
        actions.add(action);

        var cancelSchedule = new CancelScheduleImpl(() -> actions.remove(action));
        action.setCancelSchedule(cancelSchedule);
        return cancelSchedule;
    }

    @RequiredArgsConstructor
    private static class Action {
        protected final long delay;
        protected final Consumer<CancelSchedule> action;
        protected CancelSchedule cancelSchedule;

        public void perform() {
            action.accept(requireNonNull(cancelSchedule));
        }

        public void setCancelSchedule(CancelSchedule cancelSchedule) {
            this.cancelSchedule = cancelSchedule;
        }
    }

    private class FixedAction extends Action {
        public FixedAction(long delay, Consumer<CancelSchedule> action) {
            super(delay, action);
        }

        @Override
        public void perform() {
            action.accept(requireNonNull(cancelSchedule));
            if (!cancelSchedule.isCanceled()) {
                scheduleFixed(delay, action);
            }
        }
    }


    public interface CancelSchedule {
        void cancel();

        boolean isCanceled();
    }

    private static class CancelScheduleImpl implements CancelSchedule {
        private final Runnable onCancel;
        private boolean isCanceled;

        public CancelScheduleImpl(Runnable onCancel) {
            this.onCancel = onCancel;
        }

        @Override
        public void cancel() {
            isCanceled = true;
            onCancel.run();
        }

        @Override
        public boolean isCanceled() {
            return isCanceled;
        }
    }


}
