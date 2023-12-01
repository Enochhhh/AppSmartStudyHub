package com.focusedapp.smartstudyhub.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.WorkDAO;

@Service
public class WorkService {
	
	@Autowired
	WorkDAO workDAO;

}
