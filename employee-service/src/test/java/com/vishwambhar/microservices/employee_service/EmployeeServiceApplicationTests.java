package com.vishwambhar.microservices.employee_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(args = {
		"--spring.cloud.config.fail-fast=false",
		"--spring.cloud.discovery.enabled=false",
		"--eureka.client.enabled=false",
		"--eureka.client.register-with-eureka=false",
		"--eureka.client.fetch-registry=false"
})
class EmployeeServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
