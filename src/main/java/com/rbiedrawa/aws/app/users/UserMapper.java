package com.rbiedrawa.aws.app.users;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public interface UserMapper {

	static List<User> to(List<Map<String, AttributeValue>> items) {
		return items.stream()
					.map(UserMapper::to)
					.collect(Collectors.toList());
	}

	static User to(Map<String, AttributeValue> attributeMap) {
		return new User(attributeMap.get("userId").s(), attributeMap.get("email").s());
	}

	static Map<String, AttributeValue> toMap(User user) {
		return Map.of(
			"userId", AttributeValue.builder().s(user.getUserId()).build(),
			"email", AttributeValue.builder().s(user.getEmail()).build());
	}
}
