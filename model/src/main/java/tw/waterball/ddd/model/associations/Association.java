package tw.waterball.ddd.model.associations;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface Association<T> {
    void resolveAssociation(T value);
}
