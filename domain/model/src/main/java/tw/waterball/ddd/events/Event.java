package tw.waterball.ddd.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

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

     public abstract Map<String, String> toMap();
}
