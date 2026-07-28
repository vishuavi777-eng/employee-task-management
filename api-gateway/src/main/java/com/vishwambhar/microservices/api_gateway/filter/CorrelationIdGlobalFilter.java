package com.vishwambhar.microservices.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.vishwambhar.microservices.api_gateway.logging.CorrelationIdConstants.CORRELATION_ID_HEADER;

@Component
public class CorrelationIdGlobalFilter
        implements GlobalFilter, Ordered {

    private static final Logger log =
            LoggerFactory.getLogger(CorrelationIdGlobalFilter.class);


    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {
        String incomingCorrelationId =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(CORRELATION_ID_HEADER);

        incomingCorrelationId = resolveCorrelationId(incomingCorrelationId);

        String correlationId =
                StringUtils.hasText(incomingCorrelationId)
                        ? incomingCorrelationId
                        : UUID.randomUUID().toString();

        ServerHttpRequest updatedRequest =
                exchange.getRequest()
                        .mutate()
                        .headers(headers ->
                                headers.set(
                                        CORRELATION_ID_HEADER,
                                        correlationId
                                )
                        )
                        .build();

        ServerWebExchange updatedExchange =
                exchange.mutate()
                        .request(updatedRequest)
                        .build();

        updatedExchange.getResponse()
                .getHeaders()
                .set(CORRELATION_ID_HEADER, correlationId);

        log.info(
                "Incoming request: method={}, path={}, correlationId={}",
                updatedRequest.getMethod(),
                updatedRequest.getURI().getPath(),
                correlationId
        );

        return chain.filter(updatedExchange)
                .doOnSuccess(ignored ->
                        log.info(
                                "Request completed: path={}, status={}, correlationId={}",
                                updatedRequest.getURI().getPath(),
                                updatedExchange.getResponse().getStatusCode(),
                                correlationId
                        )
                )
                .doOnError(exception ->
                        log.error(
                                "Request failed: path={}, correlationId={}",
                                updatedRequest.getURI().getPath(),
                                correlationId,
                                exception
                        )
                );
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private String resolveCorrelationId(String incomingValue) {
        if (StringUtils.hasText(incomingValue)
                && incomingValue.length() <= 100) {
            return incomingValue;
        }

        return UUID.randomUUID().toString();
    }
}