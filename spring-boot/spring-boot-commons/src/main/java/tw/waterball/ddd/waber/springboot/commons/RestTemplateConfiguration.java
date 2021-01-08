package tw.waterball.ddd.waber.springboot.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.ErrorMessage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Configuration
public class RestTemplateConfiguration {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(List<ResponseErrorHandler> responseErrorHandlers) {
        var builder = new RestTemplateBuilder();
        responseErrorHandlers.forEach(builder::errorHandler);
        return builder.build();
    }

    @Bean
    public ResponseErrorHandler responseErrorHandler(ObjectMapper objectMapper) {
        return new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return !clientHttpResponse.getStatusCode().is2xxSuccessful();
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
                String body = IOUtils.toString(clientHttpResponse.getBody(), StandardCharsets.UTF_8);
                try {
                    ErrorMessage errorMessage = objectMapper.readValue(body, ErrorMessage.class);
                    errorMessage.throwException();
                } catch (JsonProcessingException ignored) {
                    throw new RuntimeException("Unrecognizable error body: " + body);
                }
            }
        };
    }
}
