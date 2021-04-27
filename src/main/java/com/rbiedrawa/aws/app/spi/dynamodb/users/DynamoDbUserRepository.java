package com.rbiedrawa.aws.app.spi.dynamodb.users;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.rbiedrawa.aws.app.domain.users.User;
import com.rbiedrawa.aws.app.domain.users.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DynamoDbUserRepository implements UserRepository {

	private static final String PK_USER_ID = "userId";

	private final DynamoDbAsyncClient dDB;

	@Value("${app.aws.dynamodb.tables.users}")
	private final String userTable;

	@Override
	public Flux<User> findAll() {
		return Mono.fromFuture(dDB.scan(ScanRequest.builder()
												   .tableName(userTable)
												   .build()))
				   .map(ScanResponse::items)
				   .map(UserMapper::to)
				   .flatMapMany(Flux::fromIterable);
	}

	@Override
	public Mono<User> findById(String userId) {
		GetItemRequest getItemRequest = GetItemRequest.builder()
													  .tableName(userTable)
													  .key(Map.of(PK_USER_ID, AttributeValue.builder().s(userId).build()))
													  .build();

		return Mono.fromFuture(dDB.getItem(getItemRequest))
				   .map(GetItemResponse::item)
				   .map(UserMapper::to);
	}

	@Override
	public Mono<User> save(User user) {
		user.setUserId(UUID.randomUUID().toString());

		log.info("Inserting new user {}", user);

		return Mono.fromFuture(dDB.putItem(PutItemRequest.builder()
														 .tableName(userTable)
														 .item(UserMapper.toMap(user))
														 .build()))
				   .map(PutItemResponse::attributes)
				   .map(map -> user);
	}

	@Override
	public Mono<User> update(String userId, User user) {
		user.setUserId(userId);
		return Mono.fromFuture(dDB.putItem(PutItemRequest.builder()
														 .tableName(userTable)
														 .item(UserMapper.toMap(user))
														 .build()))
				   .map(updateItemResponse -> user);
	}

	@Override
	public Mono<String> delete(String userId) {
		DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
															   .tableName(userTable)
															   .key(Map.of(PK_USER_ID, AttributeValue.builder().s(userId).build()))
															   .build();

		return Mono.fromFuture(dDB.deleteItem(deleteItemRequest))
				   .map(DeleteItemResponse::attributes)
				   .map(attributeValueMap -> userId);
	}

}
