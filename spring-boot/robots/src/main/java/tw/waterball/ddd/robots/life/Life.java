package tw.waterball.ddd.robots.life;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
public abstract class Life {
    private long aliveTime = 0;

    protected abstract void onNewBorn();

    public final void tick(long currentTime) {
        if (aliveTime++ == 0) {
            onNewBorn();
        }
        try {
            onTick(currentTime);
        } catch (Exception err) {
            log.error("Error", err);
        }
    }

    protected abstract void onTick(long currentTime);
}
