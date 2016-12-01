package io.tronbot.graphql.actuator.web.rest;

import java.util.Collections;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.tronbot.graphql.actuator.security.jwt.JWTConfigurer;
import io.tronbot.graphql.actuator.service.AuthenticationService;

@RestController
@RequestMapping("/api")
public class UserJWTController {
	@Inject
	private AuthenticationService authenticationService;

	@PostMapping("/authenticate")
	@Timed
	public ResponseEntity<?> authorize(HttpServletResponse response) {
		try {
			String jwt = authenticationService.authJWT();
			response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, JWTConfigurer.AUTHORIZATION_TOKEN_SCHEMA + jwt);
			return ResponseEntity.ok(new JWTToken(jwt));
		} catch (AuthenticationException exception) {
			return new ResponseEntity<>(
					Collections.singletonMap("AuthenticationException", exception.getLocalizedMessage()),
					HttpStatus.UNAUTHORIZED);
		}
	}
}
