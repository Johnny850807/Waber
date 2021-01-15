package tw.waterball.ddd.model.payment;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Getter
public class PricingItem {
    private String name;
    private String description;
    private BigDecimal price;

    public PricingItem(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
