package com.vishwambhar.microservices.api_gateway.logging;

public class CorrelationIdConstants {
    public static final String CORRELATION_ID_HEADER =
            "X-Correlation-ID";

    public static final String CORRELATION_ID_MDC_KEY =
            "correlationId";

    private CorrelationIdConstants() {}
}
