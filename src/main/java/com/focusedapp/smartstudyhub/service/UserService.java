package com.focusedapp.smartstudyhub.service;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.focusedapp.smartstudyhub.config.jwtconfig.JwtService;
import com.focusedapp.smartstudyhub.config.jwtconfig.JwtUser;
import com.focusedapp.smartstudyhub.dao.UserDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.exception.OTPCodeInvalidException;
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
public class UserService {

	@Autowired
	UserDAO userDAO;
	@Autowired
	MailSenderService mailSenderService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	JwtService jwtService;
	@Autowired
	OtpCodeService otpCodeService;

	/**
	 * 
	 * Check user existed by email
	 * 
	 * @param email
	 * @return
	 */
	public Boolean existsByEmailAndProviderLocal(String email) {
		return userDAO.existsByEmailAndProvider(email, Provider.LOCAL.getValue());
	}

	/**
	 * 
	 * Save user to database
	 * 
	 * @param user
	 * @return
	 */
	public User persistent(User user) {
		return userDAO.save(user);
	}

	/**
	 * Find User By Id And Status
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public User findByIdAndStatus(Integer id, String status) {
		return userDAO.findByIdAndStatus(id, status)
				.orElseThrow(() -> new NotFoundValueException("Not Found User By id: " + id.toString(),
						"UserService->findByIdAndStatus"));
	}

	/**
	 * 
	 * Find User by email and status ACTIVE and Provider Local
	 * 
	 * @param email
	 * @return
	 */
	public User findByEmailAndProviderLocalAndStatus(String email, String status) {
		Optional<User> user = userDAO.findByEmailAndProviderAndStatus(email, Provider.LOCAL.getValue(), status);
		if (user.isEmpty()) {
			return null;
		}
		return user.get();
	}

	/**
	 * 
	 * Find User by email and Provider Local
	 * 
	 * @param email
	 * @return
	 */
	public User findByEmailAndProviderAndStatusNotActiveE(String email, String provider) {
		Optional<User> user = userDAO.findByEmailAndProviderAndStatusNotActive(email, provider);
		if (user.isEmpty()) {
			return null;
		}
		return user.get();
	}

	/**
	 * 
	 * Find User by email
	 * 
	 * @param email
	 * @param statusFirst
	 * @param statusSecond
	 * @return
	 */
	public User findByEmail(String email) {
		User user = userDAO.findByEmail(email)
				.orElseThrow(() -> new NotFoundValueException("Not Found User By Email: " + email,
						"UserService->findByEmail"));
		return user;
	}

	/**
	 * 
	 * Create Guest User
	 * 
	 * @return
	 */
	public UserDTO createGuestUser() {

		Optional<User> userTop = userDAO.findTopByOrderByIdDesc();
		User user = null;
		if (userTop.isEmpty()) {
			user = User.builder().firstName("#GUEST ").lastName(Integer.valueOf(1).toString())
					.createdAt(new Date()).role(EnumRole.GUEST.getValue()).status(EnumStatus.ACTIVE.getValue())
					.imageUrl(ConstantUrl.DEFAULT_IMAGE).build();
		} else {
			user = User.builder().firstName("#GUEST ").lastName(Integer.valueOf(userTop.get().getId() + 1).toString())
					.createdAt(new Date()).role(EnumRole.GUEST.getValue()).status(EnumStatus.ACTIVE.getValue())
					.imageUrl(ConstantUrl.DEFAULT_IMAGE).build();
		}
		
		user = persistent(user);

		return UserDTO.builder().firstName(user.getFirstName()).lastName(user.getLastName()).id(user.getId())
				.role(user.getRole()).createdAt(user.getCreatedAt().getTime())
				.imageUrl(user.getImageUrl()).build();
	}

	/**
	 * Send OTP Code to Email
	 * 
	 * @param request
	 */
	public void sendOtpEmailToUserLocal(String email) {

		OtpCode otpCode = otpCodeService.findByEmail(email);

		String subject = "Smart Study Hub send OTP Code for account " + email +":";
		String body = "<b>OTP Code is expired after 3 minutes</b> <br>" + "<p> Here is the OTP Code: "
				+ otpCode.getOtpCode() + "</p><br>";
		mailSenderService.sendEmail(email, subject, body);
	}

	/**
	 * Resend OTP Code
	 * 
	 * @param authenticationDTO
	 * @return
	 */
	public AuthenticationDTO resendOtpCodeToUserLocal(String email) {
		
		OtpCode otpCode = otpCodeService.findByEmail(email);
		if (otpCode == null) {
			otpCode = new OtpCode();
			otpCode.setEmail(email);
		}
		
		String otpCodeStr = otpCodeService.generateOtpCode();
		otpCode.setOtpCode(otpCodeStr);
		otpCode.setOtpTimeExpiration(new Date(new Date().getTime() + 180 * 1000));
		otpCodeService.persistent(otpCode);
		sendOtpEmailToUserLocal(email);

		return AuthenticationDTO.builder().otpCode(otpCodeStr).otpTimeExpiration(otpCode.getOtpTimeExpiration().getTime())
				.build();
	}

	/**
	 * Delete user by id
	 * 
	 * @param id
	 * @return
	 */
	public UserDTO deleteById(Integer id) {

		User user = userDAO.findById(id).orElseThrow(
				() -> new NotFoundValueException("Not Found the user to delete", "UserService->deleteById"));
		userDAO.delete(user);

		return new UserDTO(user);
	}

	/**
	 * Change Password when User forgot password
	 * 
	 * @param authenticationDTO
	 * @return
	 */
	public void changePassword(AuthenticationDTO authenticationDTO) {
		User user = findByEmailAndProviderLocalAndStatus(authenticationDTO.getEmail(), EnumStatus.ACTIVE.getValue());
		
		OtpCode otpCode = otpCodeService.findByEmail(authenticationDTO.getEmail());
		if (!otpCode.getOtpCode().equals(authenticationDTO.getOtpCode()) 
				|| otpCode.getOtpTimeExpiration().before(new Date())) {
			throw new OTPCodeInvalidException("OTP Code Invalid or Expired", "AuthenticationService -> register");
		}
		
		user.setPassword(passwordEncoder.encode(authenticationDTO.getPassword()));
		persistent(user);
	}

	/**
	 * Get User by Username and provider and status
	 * 
	 * @param userName
	 * @param status
	 * @return
	 */
	public User getUserByUsernameAndProvider(String userName, String provider) {
		User user = userDAO.findByUserNameAndProvider(userName, provider);
		return user;
	}

	/**
	 * Save data when login google in database
	 * 
	 * @param username
	 */
	public User processOAuthPostLogin(OAuth2UserInfo customOAuth2User) {
		String provider = customOAuth2User.getProvider();
		User newUser = null;

		String username = customOAuth2User.getId();
		String email = customOAuth2User.getEmail();
		if (email == null) { email = "";};
		
		newUser = getUserByUsernameAndProvider(username, provider);

		if (newUser == null) {
			newUser = new User();
			newUser.setUserName(customOAuth2User.getId());
			newUser.setProvider(provider);
			newUser.setStatus(EnumStatus.ACTIVE.getValue());
			newUser.setRole(EnumRole.CUSTOMER.getValue());
			newUser.setEmail(email);
			newUser.setImageUrl(customOAuth2User.getImageUrl());
			newUser.setCreatedAt(new Date());
			newUser.setFirstName(customOAuth2User.getName());
			newUser.setLastName("");

			newUser = persistent(newUser);
		} else {
			newUser.setImageUrl(customOAuth2User.getImageUrl());
			newUser.setFirstName(customOAuth2User.getName());
			newUser.setLastName("");
		}

		return newUser;
	}

	/**
	 * Generate token
	 * 
	 * @param user
	 * @return
	 */
	public String generateToken(User user) {
		return jwtService.generateToken(new JwtUser(user));
	}
	
	/**
	 * Find User by UserName and status
	 * 
	 * @param userName
	 * @param status
	 * @return
	 */
	public User findByUserNameAndStatus(String userName, String status) {
		User user = userDAO.findByUserNameAndStatus(userName, status)
				.orElseThrow(() -> new NotFoundValueException("Not Found the user by Username and status", "UserService->findByUserNameAndStatus"));
		
		return user;
	}
	
	/**
	 * 
	 * Check user existed by UserName
	 * 
	 * @param userName
	 * @return
	 */
	public Boolean existsByUserName(String userName) {
		return userDAO.existsByUserName(userName);
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public User findByUserName(String userName) {
		User user = userDAO.findByUserName(userName)
				.orElseThrow(() -> new NotFoundValueException("Not Found the user by Username", "UserService->findByUserName"));
		
		return user;
	}
	
	/**
	 * Change status user's account
	 * 
	 * @param user
	 * @param status
	 * @return
	 */
	public AuthenticationDTO changeStatus(Integer id, String status) {
		User user = userDAO.findById(id)
				.orElseThrow(() -> new NotFoundValueException("Not found the user by id", "UserService -> changeStatus"));
		
		user.setStatus(status);
		persistent(user);
		
		return AuthenticationDTO.builder()
				.email(user.getEmail())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.role(user.getRole())
				.createdAt(user.getCreatedAt().getTime())
				.build();
		
	}
}
