package tw.waterball.ddd.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Getter @Setter
@AllArgsConstructor
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
