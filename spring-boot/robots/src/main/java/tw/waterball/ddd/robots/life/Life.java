package tw.waterball.ddd.robots.life;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
public abstract class Life {
    private long aliveTime = 0;
    private String error = "";
    private long errorCount = 0;
    private long circuitBreakThreshold = 0;
    private boolean circuitBroken = false;

    protected abstract void onNewBorn();

    public final void tick(long currentTime) {
        if (aliveTime++ == 0) {
            onNewBorn();
        }
        try {
            onTick(currentTime);
        } catch (Exception err) {
            if (!err.getMessage().equals(error)) {
                log.error("[{}] Error", getName(), err);
            }
            error = err.getMessage();
        }

    }

    public String getError() {
        return error;
    }

    public abstract String getName();

    protected abstract void onTick(long currentTime);
}
