package tw.waterball.ddd.waber.springboot.commons;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;

import java.io.IOException;
import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class ObjectMapperConfiguration {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public ObjectMapper objectMapper(List<JsonDeserializer> deserializers) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        for (JsonDeserializer deserializer : deserializers) {
            if (deserializer.handledType() == null) {
                throw new RuntimeException("Must implement JsonDeserializer.handledType() method.");
            }
            simpleModule.addDeserializer(deserializer.handledType(), deserializer);
        }
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    @Bean
    public JsonDeserializer<User> userDeserializer() {
        return new JsonObjectDeserializer<>() {
            @Override
            public Class<?> handledType() {
                return User.class;
            }

            @Override
            protected User deserializeObject(JsonParser jsonParser, DeserializationContext context,
                                             ObjectCodec codec, JsonNode tree) throws IOException {
                String type = tree.get("type").asText();
                if (type.equals(Passenger.TYPE)) {
                    return codec.treeToValue(tree, Passenger.class);
                } else if (type.equals(Driver.TYPE)) {
                    return codec.treeToValue(tree, Driver.class);
                }
                throw new IllegalStateException("Unrecognizable type of User: " + type);
            }
        };
    }
}
