package tw.waterball.ddd.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public abstract class Event {
     protected String name;

     public String getName() {
          return name;
     }
}
