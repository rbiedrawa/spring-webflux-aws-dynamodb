package com.rbiedrawa.aws.app;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.junit.jupiter.api.Tag;
import org.testcontainers.containers.GenericContainer;

@Tag("integrationTest")
@ActiveProfiles("integrationTest")
@SpringBootTest
@AutoConfigureWebTestClient
public abstract class IntegrationTest {

	private static final GenericContainer DYNAMODB_CONTAINER;

	static {
		DYNAMODB_CONTAINER = new GenericContainer<>("amazon/dynamodb-local:1.15.0");

		DYNAMODB_CONTAINER.start();
	}

	protected static String getDynamoDbEndpoint() {
		return String.format("http://%s:%s", DYNAMODB_CONTAINER.getContainerIpAddress(), DYNAMODB_CONTAINER.getMappedPort(8000));
	}

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) {
		registry.add("app.aws.dynamodb.endpoint", IntegrationTest::getDynamoDbEndpoint);
	}

}