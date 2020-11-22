package tw.waterball.ddd.model;

import tw.waterball.ddd.commons.utils.ValidationUtils;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class AggregateRoot {

    public boolean isValid() {
        return areConstraintsValid() &&
                isConsistent();
    }

    public boolean areConstraintsValid() {
        return ValidationUtils.isValid(this);
    }

    public boolean isConsistent() {
        return true;  // hook
    }

    public void validate() {
        if (!isValid()) {
            throw new IllegalStateException("Invalid Aggregate.");
        }
    }
}
