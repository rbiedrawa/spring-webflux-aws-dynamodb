package com.rbiedrawa.aws.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.rbiedrawa.aws.app.users.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

class ApplicationTest extends IntegrationTest {

	@Autowired
	private DynamoDbAsyncClient dDB;

	@Autowired
	private WebTestClient webTestClient;

	@BeforeEach
	void setUp() {
		createUserTable();
	}

	@Test
	void should_create_new_user() {
		// Given
		var user = new User(null, "test@email.com");

		// When
		var result = webTestClient.post()
								  .uri("/users")
								  .bodyValue(user)
								  .exchange();
		// Then
		result
			.expectStatus().is2xxSuccessful()
			.expectBody()
			.jsonPath("$.userId").isNotEmpty()
			.jsonPath("$.email").isEqualTo(user.getEmail());

	}


	private CreateTableResponse createUserTable() {
		return Mono.fromFuture(dDB.createTable(CreateTableRequest.builder()
																 .tableName("users")
																 .attributeDefinitions(AttributeDefinition.builder().attributeName("userId").attributeType("S").build())
																 .keySchema(KeySchemaElement.builder().attributeName("userId").keyType(KeyType.HASH).build())
																 .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(10L).writeCapacityUnits(5L).build())
																 .build()))
				   .onErrorResume(ResourceInUseException.class, e -> Mono.empty())
				   .block();
	}
}
