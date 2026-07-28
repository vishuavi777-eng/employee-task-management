package com.vishwambhar.microservices.task_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.vishwambhar.microservices.task_service.logging.CorrelationIdConstants.CORRELATION_ID_HEADER;
import static com.vishwambhar.microservices.task_service.logging.CorrelationIdConstants.CORRELATION_ID_MDC_KEY;

@Component
public class FeignCorrelationIdInterceptor
        implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String correlationId =
                MDC.get(CORRELATION_ID_MDC_KEY);

        if (StringUtils.hasText(correlationId)) {
            template.header(
                    CORRELATION_ID_HEADER,
                    correlationId
            );
        }
    }
}