package com.rbiedrawa.aws.app.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Slf4j
@Configuration
public class DynamoDbConfiguration {

	@Bean
	public DynamoDbAsyncClient dynamoDbClient(
		@Value("${app.aws.region}") String region,
		@Value("${app.aws.dynamodb.endpoint}") String dynamoDBEndpoint) {
		return DynamoDbAsyncClient.builder()
								  .endpointOverride(URI.create(dynamoDBEndpoint))
								  .region(Region.of(region))
								  .credentialsProvider(DefaultCredentialsProvider.builder().build())
								  .build();
	}
}
