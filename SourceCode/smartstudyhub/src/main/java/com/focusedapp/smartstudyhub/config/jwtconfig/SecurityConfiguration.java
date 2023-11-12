package com.focusedapp.smartstudyhub.config.jwtconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity 
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(cs -> cs.disable())
			.authorizeHttpRequests(authorize -> authorize.requestMatchers("/mobile/v1/auth/**")
					.permitAll()
					.requestMatchers("/mobile/v1/user/guest/**")
					.permitAll()
					.requestMatchers("/mobile/v1/user/customer/**").hasAnyRole("CUSTOMER", "PREMIUM")
					.requestMatchers("/mobile/v1/user/premium/**").hasAnyRole("PREMIUM")
					.requestMatchers("/mobile/v1/user/admin/**").hasAnyRole("ADMIN")
					.anyRequest()
					.authenticated())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))	
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
