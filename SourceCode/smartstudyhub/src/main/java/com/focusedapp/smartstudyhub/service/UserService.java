package com.focusedapp.smartstudyhub.service;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.UserDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumRole;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class UserService {
	
	@Autowired
	UserDAO userDAO;
	
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
	 * 
	 * Find User by email
	 * 
	 * @param email
	 * @return
	 */
	public User findByEmail(String email) {
		User user = userDAO.findByEmailAndStatus(email, EnumStatus.ACTIVE.getValue())
				.orElseThrow(() -> new NotFoundValueException("Not Found User By Email: " + email, "UserService->findByEmail"));
		return user;
	}
	
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
		
		String idEncode = Base64.getEncoder().encodeToString(user.getId().toString().getBytes());		
		return UserDTO.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.idEncode(idEncode)
				.role(user.getRole())
				.createdAt(user.getCreatedAt().getTime())
				.build();
	}

}
