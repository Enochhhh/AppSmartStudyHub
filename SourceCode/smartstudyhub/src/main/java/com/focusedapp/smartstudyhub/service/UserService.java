package com.focusedapp.smartstudyhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.UserDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class UserService {
	
	@Autowired
	UserDAO userDAO;
	
	public Boolean existsByEmail(String email) {
		return userDAO.existsByEmail(email);
	}
	
	public User persistent(User user) {
		return userDAO.save(user);
	}
	
	public User findByEmail(String email) {
		User user = userDAO.findByEmailAndStatus(email, EnumStatus.ACTIVE.getValue())
				.orElseThrow(() -> new NotFoundValueException("Not Found User By Email: " + email, ""));
		return user;
	}

}
