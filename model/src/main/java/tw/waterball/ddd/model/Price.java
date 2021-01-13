package tw.waterball.ddd.model;

import lombok.Value;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Price {
    private Double price;

    public static Price unsolved() {
        return new Price(null);
    }

    public Price(Double price) {
        this.price = price;
    }

    public boolean isResolved() {
        return price != null;
    }

    public Double getPrice() {
        return price;
    }
}
