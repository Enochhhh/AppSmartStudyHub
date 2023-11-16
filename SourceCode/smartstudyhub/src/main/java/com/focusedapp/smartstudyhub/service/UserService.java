package com.focusedapp.smartstudyhub.service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.config.jwtconfig.JwtService;
import com.focusedapp.smartstudyhub.config.jwtconfig.JwtUser;
import com.focusedapp.smartstudyhub.dao.UserDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AuthenticationDTO;
import com.focusedapp.smartstudyhub.model.custom.OAuth2UserInfo;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
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
		return user.get();
	}

	/**
	 * 
	 * Find User by email and Provider Local
	 * 
	 * @param email
	 * @return
	 */
	public User findByEmailAndProviderAndStatusNotActive(String email, String provider) {
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
						"UserService->findByEmailAndStatus"));
		return user;
	}

	/**
	 * 
	 * Create Guest User
	 * 
	 * @return
	 */
	public UserDTO createGuestUser() {

		User userTop = userDAO.findTopByOrderByIdDesc().orElseThrow(
				() -> new NotFoundValueException("Not Found The Top User", "UserService->createGuestUser"));

		User user = User.builder().firstName("#GUEST ").lastName(Integer.valueOf(userTop.getId() + 1).toString())
				.createdAt(new Date()).role(EnumRole.GUEST.getValue()).status(EnumStatus.ACTIVE.getValue()).build();

		user = persistent(user);

		return UserDTO.builder().firstName(user.getFirstName()).lastName(user.getLastName()).id(user.getId())
				.role(user.getRole()).createdAt(user.getCreatedAt().getTime()).build();
	}

	/**
	 * Generate OTP Code
	 * 
	 * @return
	 */
	public String generateOtpCode() {
		Integer number = new Random().nextInt(900000) + 100000;
		return number.toString();
	}

	/**
	 * Send OTP Code to Email
	 * 
	 * @param request
	 */
	public void sendOtpEmailToUserLocal(String email) {

		User user = findByEmailAndProviderLocalAndStatus(email, EnumStatus.ACTIVE.getValue());

		String subject = "Smart Study Hub send OTP Code for " + user.getProvider().toUpperCase() + " account:";
		String body = "<b>OTP Code is expired after 3 minutes</b> <br>" + "<p> Here is the OTP Code: "
				+ user.getOtpCode() + "</p><br>";
		mailSenderService.sendEmail(email, subject, body);
	}

	/**
	 * Resend OTP Code
	 * 
	 * @param authenticationDTO
	 * @return
	 */
	public AuthenticationDTO resendOtpCodeToUserLocal(String email) {
		User user = findByEmailAndProviderLocalAndStatus(email, EnumStatus.ACTIVE.getValue());

		if (user.getEmail() == null) {
			return null;
		}

		String otpCode = generateOtpCode();
		user.setOtpCode(otpCode);
		user.setOtpTimeExpiration(new Date(new Date().getTime() + 180 * 1000));
		persistent(user);
		sendOtpEmailToUserLocal(user.getEmail());

		return AuthenticationDTO.builder().otpCode(otpCode).otpTimeExpiration(user.getOtpTimeExpiration().getTime())
				.build();
	}

	/**
	 * Send OTP Code to change password of user account
	 * 
	 * @param email
	 * @return
	 */
	public AuthenticationDTO sendOtpCodeToChangePass(String email, User user) {

		if (!email.equals(user.getEmail())) {
			return null;
		}
		return resendOtpCodeToUserLocal(email);
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
	 * Change Password with User Logged in
	 * 
	 * @param authenticationDTO
	 * @return
	 */
	public void changePassword(AuthenticationDTO authenticationDTO, User user) {
		user.setPassword(passwordEncoder.encode(authenticationDTO.getPassword()));
		persistent(user);
	}

	/**
	 * Change Password when User forgot password
	 * 
	 * @param authenticationDTO
	 * @return
	 */
	public void changePassword(AuthenticationDTO authenticationDTO) {
		User user = findByEmailAndProviderLocalAndStatus(authenticationDTO.getEmail(), EnumStatus.ACTIVE.getValue());
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
}
