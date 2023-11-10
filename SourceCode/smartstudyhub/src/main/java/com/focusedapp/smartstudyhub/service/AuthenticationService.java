package com.focusedapp.smartstudyhub.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.config.jwtconfig.JwtService;
import com.focusedapp.smartstudyhub.config.jwtconfig.JwtUser;
import com.focusedapp.smartstudyhub.exception.ValueExistedException;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AuthenticationDTO;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumRole;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class AuthenticationService {

	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;
	@Autowired
	AuthenticationManager authenticationManager;	

	/**
	 * Service Register User
	 * 
	 * @param request
	 * @return
	 */
	public AuthenticationDTO register(AuthenticationDTO request) {

		if (userService.existsByEmail(request.getEmail())) {
			throw new ValueExistedException("Email Invalid or Existed", "/mobile/v1/auth/register");
		}

		String otpCode = userService.generateOtpCode();
		User user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).createdAt(new Date())
				.role(EnumRole.CUSTOMER.getValue()).otpCode(otpCode)
				.otpTimeExpiration(new Date(new Date().getTime() + 180 * 1000)).status(EnumStatus.ACTIVE.getValue())
				.build();
		userService.persistent(user);
		request.setOtpCode(otpCode);
		request.setOtpTimeExpiration(user.getOtpTimeExpiration().getTime());
		
		userService.sendOtpEmailToUser(request.getEmail());
		
		return request;

	}

	/**
	 * Service Authenticate User
	 * 
	 * @param request
	 * @return
	 */
	public AuthenticationDTO authenticate(AuthenticationDTO request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		User user = userService.findByEmailAndStatus(request.getEmail(), EnumStatus.ACTIVE.getValue());

		return AuthenticationDTO.builder().email(user.getEmail()).firstName(user.getFirstName())
				.lastName(user.getLastName()).role(user.getRole()).createdAt(user.getCreatedAt().getTime())
				.token(jwtService.generateToken(new JwtUser(user))).build();
	}
	
	/**
	 * Check user authenticated
	 * 
	 * @param authentication
	 * @return
	 */
	public Boolean isAuthenticated(Authentication authentication) {
		return authentication != null
				&& !(authentication instanceof AnonymousAuthenticationToken)
				&& authentication.isAuthenticated();
	}
	
	/**
	 * Get Authenticated User
	 * 
	 * @return
	 */
	public User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
		if (isAuthenticated(authentication)) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof JwtUser) {
				JwtUser jwtUser = (JwtUser) principal;
				return jwtUser.getUser();
			}
		} else {
			throw new RuntimeException("Login is required");
		}
		return null;
	}
	
	/**
	 * Resend OTP Code
	 * 
	 * @param id
	 * @return
	 */
	public AuthenticationDTO resendOtpCode(String email) {
		return userService.resendOtpCode(email);
	}
	
	/**
	 * Delete user registered
	 * 
	 * @return
	 */
	public UserDTO deleteUserRegistered(Integer id) {
		return userService.deleteById(id);
	}

}
