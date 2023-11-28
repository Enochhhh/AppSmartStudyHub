package com.focusedapp.smartstudyhub.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.focusedapp.smartstudyhub.config.jwtconfig.JwtService;
import com.focusedapp.smartstudyhub.config.jwtconfig.JwtUser;
import com.focusedapp.smartstudyhub.exception.AccountBannedException;
import com.focusedapp.smartstudyhub.exception.AccountDeletedException;
import com.focusedapp.smartstudyhub.exception.OTPCodeInvalidException;
import com.focusedapp.smartstudyhub.exception.ValueExistedException;
import com.focusedapp.smartstudyhub.model.OtpCode;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AuthenticationDTO;
import com.focusedapp.smartstudyhub.model.custom.OAuth2UserInfo;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;
import com.focusedapp.smartstudyhub.util.enumerate.EnumRole;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.Provider;

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
	@Autowired
	OtpCodeService otpCodeService;

	/**
	 * Service Register User
	 * 
	 * @param request
	 * @return
	 */
	public AuthenticationDTO register(AuthenticationDTO request) {

		if (userService.existsByUserName(request.getEmail()) 
				|| userService.existsByEmailAndProviderLocal(request.getEmail())) {
			throw new ValueExistedException("Email Invalid or Existed", "/mobile/v1/auth/register");
		}
		
		OtpCode otpCode = otpCodeService.findByEmail(request.getEmail());
		if (!otpCode.getOtpCode().equals(request.getOtpCode()) 
				|| otpCode.getOtpTimeExpiration().before(new Date())) {
			throw new OTPCodeInvalidException("OTP Code Invalid or Expired", "AuthenticationService -> register");
		}

		User user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
				.userName(request.getEmail())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).createdAt(new Date())
				.role(EnumRole.CUSTOMER.getValue())
				.provider(Provider.LOCAL.getValue())
				.imageUrl(ConstantUrl.DEFAULT_IMAGE)
				.status(EnumStatus.ACTIVE.getValue())
				.build();
		userService.persistent(user);
		
		return request;

	}

	/**
	 * Service Authenticate User
	 * 
	 * @param request
	 * @return
	 */
	public AuthenticationDTO authenticate(AuthenticationDTO request) {
		
		User user = userService.findByUserName(request.getEmail());
		if (user != null) {
			if (user.getStatus().equals(EnumStatus.BANNED.getValue())) {
				throw new AccountBannedException("Account was banned", "/login/local-account");
			} else if (user.getStatus().equals(EnumStatus.DELETED.getValue())) {
				throw new AccountDeletedException("Account was deleted", "/login/local-account");
			}
		}
			
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		return AuthenticationDTO.builder().email(user.getEmail()).firstName(user.getFirstName())
				.lastName(user.getLastName()).role(user.getRole()).createdAt(user.getCreatedAt().getTime())
				.imageUrl(user.getImageUrl())
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
			} else if (principal instanceof UserDetails) {
				JwtUser jwtUser = (JwtUser) principal;
				return jwtUser.getUser();
			} else if (principal instanceof OAuth2User) {
				OAuth2UserInfo oAuth2UserInfo = (OAuth2UserInfo) principal;
				User user = userService.getUserByUsernameAndProvider(oAuth2UserInfo.getId(), oAuth2UserInfo.getProvider());
				if (user == null) {
					throw new RuntimeException("Login is required");
				}
				return user;
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
		return userService.resendOtpCodeToUserLocal(email);
	}
	
	/**
	 * Delete user registered
	 * 
	 * @return
	 */
	public UserDTO deleteUserRegistered(Integer id) {
		return userService.deleteById(id);
	}
	
	/**
	 * Authenticate to recover account
	 * 
	 * @param request
	 * @return
	 */
	public AuthenticationDTO authenticateToRecoverAccount(AuthenticationDTO request) {
		User user = userService.findByUserName(request.getEmail());
			
		if (user.getUserName().equals(request.getEmail())
				&& passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			return AuthenticationDTO.builder().email(user.getEmail()).firstName(user.getFirstName())
					.lastName(user.getLastName()).role(user.getRole()).createdAt(user.getCreatedAt().getTime())
					.id(user.getId()).build();
		}

		return null;
	}

}
