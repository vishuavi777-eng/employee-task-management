package com.vishwambhar.microservices.task_service.client;

import com.vishwambhar.microservices.task_service.dto.EmployeeValidationResponse;
import com.vishwambhar.microservices.task_service.exception.EmployeeServiceUnavailableException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

//@Component
public class RestEmployeeClient implements EmployeeClient {

    private final RestClient employeeRestClient;

    public RestEmployeeClient(
            RestClient employeeRestClient
    ) {
        this.employeeRestClient = employeeRestClient;
    }

    @Override
    public EmployeeValidationResponse validateEmployee(
            Long employeeId
    ) {

        try {
            EmployeeValidationResponse response =
                    employeeRestClient
                            .get()
                            .uri(
                                    "/api/v1/employees/{employeeId}/validation",
                                    employeeId
                            )
                            .retrieve()

                            // Employee Service returned 5xx.
                            .onStatus(
                                    HttpStatusCode::is5xxServerError,
                                    (request, responseData) -> {
                                        throw new EmployeeServiceUnavailableException(
                                                "Employee Service returned an error: "
                                                        + responseData.getStatusCode()
                                        );
                                    }
                            )

                            // Unexpected 4xx from Employee Service.
                            .onStatus(
                                    HttpStatusCode::is4xxClientError,
                                    (request, responseData) -> {
                                        throw new EmployeeServiceUnavailableException(
                                                "Employee Service rejected the validation request: "
                                                        + responseData.getStatusCode()
                                        );
                                    }
                            )

                            .body(EmployeeValidationResponse.class);

            if (response == null) {
                throw new EmployeeServiceUnavailableException(
                        "Employee Service returned an empty response"
                );
            }

            return response;

        } catch (EmployeeServiceUnavailableException exception) {

            // Preserve our meaningful custom exception.
            throw exception;

        } catch (ResourceAccessException exception) {

            /*
             * Handles low-level I/O problems:
             *
             * - Connection refused
             * - Connect timeout
             * - Read timeout
             * - DNS/network error
             */
            throw new EmployeeServiceUnavailableException(
                    "Employee Service is unavailable or did not respond in time",
                    exception
            );

        } catch (RestClientException exception) {

            /*
             * Handles other RestClient failures:
             *
             * - Invalid response
             * - JSON conversion failure
             * - HTTP client failure
             */
            throw new EmployeeServiceUnavailableException(
                    "Failed to communicate with Employee Service",
                    exception
            );
        }
    }
}