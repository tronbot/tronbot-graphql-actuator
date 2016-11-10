package io.tronbot.graphql.actuator.web.rest;

import java.util.Collections;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.tronbot.graphql.actuator.security.jwt.JWTConfigurer;
import io.tronbot.graphql.actuator.security.jwt.TokenProvider;

@RestController
@RequestMapping("/api")
public class UserJWTController {

	@Inject
	private TokenProvider tokenProvider;

	@Inject
	private AuthenticationManager authenticationManager;

	@Inject
	private InMemoryUserDetailsManager userDetailsManager;

	@PostMapping("/authenticate")
	@Timed
	public ResponseEntity<?> authorize(HttpServletResponse response) {
		UserDetails mockUser = userDetailsManager.loadUserByUsername("user");
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				mockUser.getUsername(), mockUser.getPassword());

		try {
			Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			boolean rememberMe = true;
			String jwt = tokenProvider.createToken(authentication, rememberMe);
			response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
			return ResponseEntity.ok(new JWTToken(jwt));
		} catch (AuthenticationException exception) {
			return new ResponseEntity<>(
					Collections.singletonMap("AuthenticationException", exception.getLocalizedMessage()),
					HttpStatus.UNAUTHORIZED);
		}
	}
}
