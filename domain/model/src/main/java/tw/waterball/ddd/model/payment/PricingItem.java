package tw.waterball.ddd.model.payment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
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
