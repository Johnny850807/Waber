package tw.waterball.chaos.core.md5;

import static java.util.Arrays.stream;

import tw.waterball.chaos.api.Chaos;
import tw.waterball.chaos.api.FunValue;

/**
 * Define:
 *
 * @author Waterball (johnny850807@gmail.com)
 */
public abstract class Md5Chaos implements Chaos {
    private final Criteria criteria;
    private final String name;
    private boolean executed;
    private boolean killed;

    public Md5Chaos() {
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
        byte[] digest = ((Md5FunValue) funValue).getDigest();
        executed = criteria.match(digest);
        if (executed) {
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

    @Override
    public void kill() {
        if (isExecuted()) {
            killed = true;
            onKilled();
        }
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public boolean isAlive() {
        return isExecuted() && !isKilled();
    }

    @Override
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

    protected static Criteria always() {
        return digest -> true;
    }

    protected static Criteria never() {
        return digest -> false;
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

    protected static Criteria areZeros(int... positions) {
        validatePositions(positions);
        return digest -> stream(positions)
                .allMatch(i -> i == 0);
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
