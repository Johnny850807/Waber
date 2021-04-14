package tw.waterball.ddd.model.base;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class Entity<ID> {
    protected ID id;

    public Entity() {
    }

    public Entity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
