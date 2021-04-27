package com.rbiedrawa.aws.app.api.users;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UserApi {
	private final UserHandler userHandler;

	@Bean
	RouterFunction<ServerResponse> usersApi() {
		return route(GET("/users"), request -> userHandler.findAllUsers())
			.andRoute(POST("/users"), userHandler::createUser)
			.andRoute(GET("/users/{userId}"), userHandler::findUser)
			.andRoute(DELETE("/users/{userId}"), userHandler::delete)
			.andRoute(PUT("/users/{userId}"), userHandler::update);
	}
}
