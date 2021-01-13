package tw.waterball.ddd.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
@Setter
@NoArgsConstructor
public class Price {
    private double price;

    public Price(Double price) {
        this.price = price;
    }

}
