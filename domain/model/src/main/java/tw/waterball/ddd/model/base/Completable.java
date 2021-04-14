package tw.waterball.ddd.model.base;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface Completable {

    OrElse ifComplete();

    interface OrElse {

    }
}
