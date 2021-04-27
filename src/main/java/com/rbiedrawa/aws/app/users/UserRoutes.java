package com.rbiedrawa.aws.app.users;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class UserRoutes {
	private final UserRepository userRepository;

	@Bean
	RouterFunction<ServerResponse> users() {
		return route(GET("/users"), findAllUsers())
			.andRoute(POST("/users"), this::createUser)
			.andRoute(GET("/users/{userId}"), this::findUser)
			.andRoute(DELETE("/users/{userId}"), this::delete)
			.andRoute(PUT("/users/{userId}"), this::update);
	}

	private Mono<ServerResponse> findUser(ServerRequest request) {
		final String userId = request.pathVariable("userId");
		return userRepository.findById(userId)
							 .flatMap(user -> ServerResponse.ok().body(fromValue(user)));
	}

	private Mono<ServerResponse> delete(ServerRequest request) {
		final String userId = request.pathVariable("userId");
		return userRepository.delete(userId)
							 .flatMap(s -> ServerResponse.ok().build());
	}

	private Mono<ServerResponse> update(ServerRequest request) {
		final String userId = request.pathVariable("userId");
		return request.bodyToMono(User.class)
					  .flatMap(user -> userRepository.update(userId, user))
					  .flatMap(user -> ServerResponse.ok().body(fromValue(user)));
	}

	private Mono<ServerResponse> createUser(ServerRequest request) {
		return request.bodyToMono(User.class)
					  .flatMap(userRepository::save)
					  .flatMap(user -> ServerResponse.ok().body(fromValue(user)));
	}

	private HandlerFunction<ServerResponse> findAllUsers() {
		return request -> userRepository.findAll()
										.collect(Collectors.toList())
										.flatMap(users -> ServerResponse.ok().body(fromValue(users)));
	}
}
