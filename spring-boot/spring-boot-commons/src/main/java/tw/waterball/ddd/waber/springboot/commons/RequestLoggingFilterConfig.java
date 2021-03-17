package tw.waterball.ddd.waber.springboot.commons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * -Dotel.jaeger.service.name=waber-match'-Dlogging.pattern.console=%d{yyyy-MM-ddHH:mm:ss}-%logger{36}-%msgtraceID=%X{traceId}%n'
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class RequestLoggingFilterConfig {
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);
        return filter;
    }
}