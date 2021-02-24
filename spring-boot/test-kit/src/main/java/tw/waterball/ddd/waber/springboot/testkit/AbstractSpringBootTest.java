package tw.waterball.ddd.waber.springboot.testkit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractSpringBootTest {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;


    @SneakyThrows
    protected String toJson(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T getBody(ResultActions resultActions, Class<T> type) throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(resultActions
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), type);
    }
    protected <T> T getBody(ResultActions resultActions, TypeReference<T> type) throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(resultActions
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), type);
    }
}