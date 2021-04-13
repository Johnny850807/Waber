package tw.waterball.waber.chaos.core;

import static java.util.Arrays.stream;

import tw.waterball.waber.chaos.api.Chaos;
import tw.waterball.waber.chaos.api.FunValue;
import tw.waterball.waber.chaos.springboot.chaos.profiles.ChaosEngineering;

/**
 * Define:
 *
 * @author Waterball (johnny850807@gmail.com)
 */
@ChaosEngineering
public abstract class WaberChaos implements Chaos {
    protected final Criteria criteria;
    protected String name;
    private boolean performed;
    private boolean killed;

    public WaberChaos() {
        this.criteria = criteria();
        this.name = getName();
    }

    protected abstract Criteria criteria();

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void execute(FunValue funValue) {
        byte[] digest = ((WaberFunValue) funValue).getDigest();
        performed = criteria.match(digest);
        if (performed) {
            onPerform(digest);
        } else {
            onNotMatch();
        }
    }

    protected void onPerform(byte[] digest) {
    }

    protected void onNotMatch() {
    }

    protected void onKilled() {

    }

    public boolean isPerformed() {
        return performed;
    }

    @Override
    public void kill() {
        if (isPerformed()) {
            killed = true;
            onKilled();
        }
    }

    public boolean isAlive() {
        return isPerformed() && !isKilled();
    }

    public boolean isKilled() {
        return killed;
    }

    public interface Criteria {
        boolean match(byte[] digest);
    }

    protected static Criteria or(Criteria c1, Criteria c2) {
        return digest -> c1.match(digest) || c2.match(digest);
    }

    protected static Criteria or(Criteria... criteria) {
        return digest -> stream(criteria).anyMatch(c -> c.match(digest));
    }

    protected static Criteria not(Criteria c) {
        return digest -> !c.match(digest);
    }

    protected static Criteria and(Criteria c1, Criteria c2) {
        return digest -> c1.match(digest) && c2.match(digest);
    }

    protected static Criteria and(Criteria... criteria) {
        return digest -> stream(criteria).allMatch(c -> c.match(digest));
    }

    protected static Criteria positiveNumberAtPositions(int... positions) {
        validatePositions(positions);
        return digest -> stream(positions)
                .allMatch(i -> digest[i] > 0);
    }

    protected static Criteria negativeNumberAtPositions(int... positions) {
        validatePositions(positions);
        return digest -> stream(positions)
                .allMatch(i -> digest[i] < 0);
    }

    private static void validatePositions(int... positions) {
        if (positions.length == 0) {
            throw new IllegalArgumentException("No positions?");
        }
        stream(positions).forEach(i -> {
            if (i < 0 || i >= 16) { // md5 produces a byte[16]
                throw new IllegalArgumentException("Positions must be >= 0 and < 16");
            }
        });

    }

}
