package tw.waterball.ddd.robots.life;

import tw.waterball.ddd.robots.Timeline;

import java.util.function.Consumer;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public abstract class ScheduledLife extends Life {
    protected Timeline timeline = new Timeline();

    @Override
    public void onTick(long currentTime) {
        timeline.tick(currentTime);
    }

    public void schedule(long delay, Consumer<Timeline.CancelSchedule> action) {
        timeline.schedule(delay, action);
    }

    public Timeline.CancelSchedule scheduleFixed(long delay, Consumer<Timeline.CancelSchedule> action) {
        return timeline.scheduleFixed(delay, action);
    }


}
