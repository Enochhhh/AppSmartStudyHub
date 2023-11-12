package com.focusedapp.smartstudyhub.config.jwtconfig;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.focusedapp.smartstudyhub.model.custom.CustomOAuth2User;
import com.focusedapp.smartstudyhub.service.CustomOAuth2UserService;
import com.focusedapp.smartstudyhub.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity 
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final CustomOAuth2UserService oauthUserService;
	private final UserService userService;
	private final DataSource dataSource;

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
			.formLogin(f -> f.disable())
			.oauth2Login(o -> o.userInfoEndpoint(ui -> ui.userService(oauthUserService))
					.successHandler(new AuthenticationSuccessHandler() {
						
						@Override
						public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
								Authentication authentication) throws IOException, ServletException {
							// TODO Auto-generated method stub
							CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
							 
							userService.processOAuthPostLogin(oauthUser.getEmail(), oauthUser);
				 
				            response.sendRedirect("/mobile/v1/auth/get-user-google/".concat(oauthUser.getEmail()));
						}
					}))
			.logout(l -> l.permitAll())
			.rememberMe(r -> r.tokenRepository(persistentTokenRepository()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))	
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		
		return tokenRepository;
	}
}
