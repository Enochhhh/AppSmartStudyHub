package com.focusedapp.smartstudyhub.service;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.UserDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AuthenticationDTO;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumRole;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class UserService {
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	MailSenderService mailSenderService;
	
	/**
	 * 
	 * Check user existed by email
	 * 
	 * @param email
	 * @return
	 */
	public Boolean existsByEmail(String email) {
		return userDAO.existsByEmail(email);
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
	 * Find User by email and status ACTIVE
	 * 
	 * @param email
	 * @return
	 */
	public User findByEmailAndStatus(String email, String status) {
		User user = userDAO.findByEmailAndStatus(email, status)
				.orElseThrow(() -> new NotFoundValueException("Not Found User By Email: " + email, "UserService->findByEmailAndStatus"));
		return user;
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
				.orElseThrow(() -> new NotFoundValueException("Not Found User By Email: " + email, "UserService->findByEmailAndStatus"));
		return user;
	}
	
	/**
	 * 
	 * Create Guest User
	 * 
	 * @return
	 */
	public UserDTO createGuestUser() {
		
		User userTop = userDAO.findTopByOrderByIdDesc()
				.orElseThrow(() -> new NotFoundValueException("Not Found The Top User", "UserService->createGuestUser"));
		
		User user = User.builder()
				.firstName("#GUEST ")
				.lastName(Integer.valueOf(userTop.getId() + 1).toString())
				.createdAt(new Date())
				.role(EnumRole.GUEST.getValue())
				.status(EnumStatus.ACTIVE.getValue())
				.build();
		
		user = persistent(user);
			
		return UserDTO.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.id(user.getId())
				.role(user.getRole())
				.createdAt(user.getCreatedAt().getTime())
				.build();
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
	public void sendOtpEmailToUser(String email) {
		
		User user = findByEmailAndStatus(email, EnumStatus.ACTIVE.getValue());
		
		String subject = "Smart Study Hub send OTP Code";
		String body = "<b>OTP Code is expired after 3 minutes</b> <br>"			
				+ "<p> Here is the OTP Code: " + user.getOtpCode() + "</p><br>";
		mailSenderService.sendEmail(email, subject, body);
	}
	
	/**
	 * Resend OTP Code
	 * 
	 * @param authenticationDTO
	 * @return
	 */
	public AuthenticationDTO resendOtpCode(Integer id) {
		User user = findByIdAndStatus(id, EnumStatus.ACTIVE.getValue());
		
		if (user.getEmail() == null) {
			return null;
		}
		
		String otpCode = generateOtpCode();
		user.setOtpCode(otpCode);
		user.setOtpTimeExpiration(new Date(new Date().getTime() + 180 * 1000));
		persistent(user);
		
		return AuthenticationDTO.builder()
				.otpCode(otpCode)
				.otpTimeExpiration(user.getOtpTimeExpiration().getTime())
				.build();
	}
	
	/**
	 * Delete user by id
	 * 
	 * @param id
	 * @return
	 */
	public UserDTO deleteById(Integer id) {
		
		User user = userDAO.findById(id).orElseThrow(() -> new NotFoundValueException(
				"Not Found the user to delete", "UserService->deleteById"));
		userDAO.delete(user);
		
		return new UserDTO(user);
	}

}
