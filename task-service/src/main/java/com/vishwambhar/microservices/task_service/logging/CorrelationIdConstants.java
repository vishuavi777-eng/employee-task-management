package com.vishwambhar.microservices.task_service.logging;

public class CorrelationIdConstants {
    public static final String CORRELATION_ID_HEADER =
            "X-Correlation-ID";

    public static final String CORRELATION_ID_MDC_KEY =
            "correlationId";

    private CorrelationIdConstants() {}
}
