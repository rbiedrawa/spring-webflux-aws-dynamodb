package com.rbiedrawa.aws.app.spi.dynamodb.users;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rbiedrawa.aws.app.domain.users.User;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public interface UserMapper {

	static List<User> to(List<Map<String, AttributeValue>> items) {
		return items.stream()
					.map(UserMapper::to)
					.collect(Collectors.toList());
	}

	static User to(Map<String, AttributeValue> attributeMap) {
		return User.builder()
				   .userId(attributeMap.get("userId").s())
				   .email(attributeMap.get("email").s())
				   .build();
	}

	static Map<String, AttributeValue> toMap(User user) {
		return Map.of(
			"userId", AttributeValue.builder().s(user.getUserId()).build(),
			"email", AttributeValue.builder().s(user.getEmail()).build());
	}
}
