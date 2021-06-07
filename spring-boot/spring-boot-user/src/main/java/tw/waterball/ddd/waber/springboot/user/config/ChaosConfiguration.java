package tw.waterball.ddd.waber.springboot.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tw.waterball.chaos.annotations.ChaosEngineering;
import tw.waterball.chaos.annotations.EnableChaosClient;
import tw.waterball.chaos.config.TcpClientConfiguration;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ChaosEngineering
@Import({TcpClientConfiguration.class})
@Configuration
public class ChaosConfiguration {
}
