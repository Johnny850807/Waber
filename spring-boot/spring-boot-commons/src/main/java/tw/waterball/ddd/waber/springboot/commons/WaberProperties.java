package tw.waterball.ddd.waber.springboot.commons;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import tw.waterball.ddd.commons.model.BaseUrl;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Data
@ConfigurationProperties("waber")
public class WaberProperties {
    @NestedConfigurationProperty
    private Client client;

    @Data
    public static class Client {
        @NestedConfigurationProperty
        private BaseUrl userService;
        @NestedConfigurationProperty
        private BaseUrl matchService;
        @NestedConfigurationProperty
        private BaseUrl tripService;
        @NestedConfigurationProperty
        private BaseUrl paymentService;
        @NestedConfigurationProperty
        private BaseUrl brokerService;
    }
}

