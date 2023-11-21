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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.focusedapp.smartstudyhub.exception.AccessDeniedHandlerException;
import com.focusedapp.smartstudyhub.exception.AuthenticationEntryPointException;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.OAuth2UserInfo;
import com.focusedapp.smartstudyhub.service.CustomOAuth2UserService;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

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
			.authorizeHttpRequests(authorize -> authorize.requestMatchers("/mobile/v1/auth/**", "/oauth2/**")
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
					//.authorizationEndpoint(au -> au.baseUri("/oauth2/authorize"))
					//.redirectionEndpoint(r -> r.baseUri("/oauth2/callback/*"))
					.successHandler(new AuthenticationSuccessHandler() {
						
						@Override
						public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
								Authentication authentication) throws IOException, ServletException {
							// TODO Auto-generated method stub
							OAuth2UserInfo oauthUser = (OAuth2UserInfo) authentication.getPrincipal();
							 
							User user = userService.processOAuthPostLogin(oauthUser);
							if (user.getStatus().equals(EnumStatus.DELETED.getValue())) {
								response.sendRedirect(ConstantUrl.CLIENT_URL + "/account-deleted");
							} else if (user.getStatus().equals(EnumStatus.BANNED.getValue())) {
								response.sendRedirect(ConstantUrl.CLIENT_URL + "/account-banned");
							}
							else {
								// response.sendRedirect(ConstantUrl.CLIENT_URL + "?token=" + userService.generateToken(user));
								response.sendRedirect("https://youtube.com/");
							}				           
						}
					})
					.failureHandler(new AuthenticationFailureHandler() {
						
						@Override
						public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
								AuthenticationException exception) throws IOException, ServletException {
							// TODO Auto-generated method stub
				            response.sendRedirect(ConstantUrl.CLIENT_URL + "/error");
						}
					})) 
			.logout(l -> l.permitAll())
			.rememberMe(r -> r.tokenRepository(persistentTokenRepository()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))	
			.authenticationProvider(authenticationProvider)
			//.exceptionHandling(e -> e.accessDeniedHandler(accessDeniedHandler())
					//.authenticationEntryPoint(authenticationEntryPoint()))
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		
		return tokenRepository;
	}
	
	@Bean
	AccessDeniedHandlerException accessDeniedHandler() {
		return new AccessDeniedHandlerException();
	}

	@Bean
	AuthenticationEntryPointException authenticationEntryPoint() {
		return new AuthenticationEntryPointException();
	}
}
