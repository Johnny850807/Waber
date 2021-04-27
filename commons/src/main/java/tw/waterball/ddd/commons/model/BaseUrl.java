package tw.waterball.ddd.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseUrl {
    private String scheme;
    private String host;
    private int port;

    public String withPath(String path) {
        return String.format("%s%s", this.toString(), path);
    }

    @Override
    public String toString() {
        return String.format("%s://%s:%d", scheme, host, port);
    }
}
