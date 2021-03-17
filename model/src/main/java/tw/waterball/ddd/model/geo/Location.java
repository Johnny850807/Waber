package tw.waterball.ddd.model.geo;

import lombok.*;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private double latitude;
    private double longitude;
}
