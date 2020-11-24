package tw.waterball.ddd.model.match;

import tw.waterball.ddd.model.AggregateRoot;

import javax.validation.constraints.Size;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class Activity extends AggregateRoot {
    private int id;

    @Size(min = 1, max = 10)
    private String name;
}
