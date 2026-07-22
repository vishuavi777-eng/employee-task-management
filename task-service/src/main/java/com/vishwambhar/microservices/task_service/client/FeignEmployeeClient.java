package com.vishwambhar.microservices.task_service.client;

import com.vishwambhar.microservices.task_service.dto.EmployeeValidationResponse;
import com.vishwambhar.microservices.task_service.exception.EmployeeServiceUnavailableException;
import feign.FeignException;
import feign.RetryableException;
import org.springframework.stereotype.Component;

@Component
public class FeignEmployeeClient implements EmployeeClient {

    private final EmployeeFeignApi employeeFeignApi;

    public FeignEmployeeClient(
            EmployeeFeignApi employeeFeignApi
    ) {
        this.employeeFeignApi = employeeFeignApi;
    }

    @Override
    public EmployeeValidationResponse validateEmployee(
            Long employeeId
    ) {

        try {
            EmployeeValidationResponse response =
                    employeeFeignApi.validateEmployee(employeeId);

            if (response == null) {
                throw new EmployeeServiceUnavailableException(
                        "Employee Service returned an empty response"
                );
            }

            return response;

        } catch (RetryableException exception) {

            /*
             * Usually includes:
             *
             * - Connection refused
             * - Connect timeout
             * - Read timeout
             * - Temporary network failure
             */
            throw new EmployeeServiceUnavailableException(
                    "Employee Service is unavailable or did not respond in time",
                    exception
            );

        } catch (FeignException.FeignServerException exception) {

            /*
             * Employee Service returned HTTP 5xx.
             */
            throw new EmployeeServiceUnavailableException(
                    "Employee Service returned server error: "
                            + exception.status(),
                    exception
            );

        } catch (FeignException.FeignClientException exception) {

            /*
             * Unexpected HTTP 4xx response.
             *
             * Our validation endpoint normally returns HTTP 200
             * even when the employee does not exist.
             */
            throw new EmployeeServiceUnavailableException(
                    "Employee Service rejected the validation request: "
                            + exception.status(),
                    exception
            );

        } catch (FeignException exception) {

            throw new EmployeeServiceUnavailableException(
                    "Failed to communicate with Employee Service",
                    exception
            );
        }
    }
}