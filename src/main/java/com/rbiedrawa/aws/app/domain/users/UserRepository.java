package com.rbiedrawa.aws.app.domain.users;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
	Flux<User> findAll();

	Mono<User> findById(String userId);

	Mono<User> save(User user);

	Mono<User> update(String userId, User user);

	Mono<String> delete(String userId);
}
