package tw.waterball.ddd.model.base;

import tw.waterball.ddd.commons.utils.ValidationUtils;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class AggregateRoot<ID> extends Entity<ID> {
    public AggregateRoot() {
    }

    public AggregateRoot(ID id) {
        super(id);
    }

    public boolean isValid() {
        return ValidationUtils.isValid(this);
    }

    public void validate() {
        if (!isValid()) {
            throw new IllegalStateException("Invalid Aggregate.");
        }
    }
}
