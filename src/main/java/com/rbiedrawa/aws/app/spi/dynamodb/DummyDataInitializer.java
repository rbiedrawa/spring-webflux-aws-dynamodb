package com.rbiedrawa.aws.app.spi.dynamodb;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.rbiedrawa.aws.app.domain.users.User;
import com.rbiedrawa.aws.app.domain.users.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

// Just for demo purposes!
@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!integrationTest")
public class DummyDataInitializer {

	private final UserRepository userRepository;

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReadyEvent() {
		userRepository.findAll()
					  .switchIfEmpty(Flux.defer(this::insertDummyUsers))
					  .subscribe(user -> log.info("DynamoDb: {}", user));
	}

	private Flux<User> insertDummyUsers() {
		return Flux.just("user@test.com", "admin@test.com")
				   .map(email -> User.builder().email(email).build())
				   .flatMap(userRepository::save);
	}
}
