package io.tronbot.graphql.actuator.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import io.tronbot.graphql.actuator.security.jwt.TokenProvider;

/**
 * 
 * @Author Juanyong Zhang
 * @Date Dec 1, 2016
 */
@Service
public class AuthenticationService {
	protected final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
	
	@Inject
	private TokenProvider tokenProvider;

	@Inject
	private AuthenticationManager authenticationManager;

	@Inject
	private InMemoryUserDetailsManager userDetailsManager;

	
	public String authJWT() throws AuthenticationException{
		UserDetails mockUser = userDetailsManager.loadUserByUsername("user");
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				mockUser.getUsername(), mockUser.getPassword());
		Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		boolean rememberMe = true;
		return tokenProvider.createToken(authentication, rememberMe);

	}
}
