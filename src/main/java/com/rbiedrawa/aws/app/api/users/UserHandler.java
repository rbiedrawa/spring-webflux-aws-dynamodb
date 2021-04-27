package com.rbiedrawa.aws.app.api.users;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.rbiedrawa.aws.app.domain.users.User;
import com.rbiedrawa.aws.app.domain.users.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {
	private final UserRepository userRepository;

	public Mono<ServerResponse> findUser(ServerRequest request) {
		final String userId = request.pathVariable("userId");
		return userRepository.findById(userId)
							 .flatMap(user -> ServerResponse.ok().body(fromValue(user)));
	}

	public Mono<ServerResponse> delete(ServerRequest request) {
		final String userId = request.pathVariable("userId");
		return userRepository.delete(userId)
							 .flatMap(s -> ServerResponse.ok().build());
	}

	public Mono<ServerResponse> update(ServerRequest request) {
		final String userId = request.pathVariable("userId");
		return request.bodyToMono(User.class)
					  .flatMap(user -> userRepository.update(userId, user))
					  .flatMap(user -> ServerResponse.ok().body(fromValue(user)));
	}

	public Mono<ServerResponse> createUser(ServerRequest request) {
		return request.bodyToMono(User.class)
					  .flatMap(userRepository::save)
					  .flatMap(user -> ServerResponse.ok().body(fromValue(user)));
	}

	public Mono<ServerResponse> findAllUsers() {
		return userRepository.findAll()
							 .collect(Collectors.toList())
							 .flatMap(users -> ServerResponse.ok().body(fromValue(users)));
	}
}
