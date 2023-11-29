package com.focusedapp.smartstudyhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.ProjectDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class ProjectService {

	@Autowired
	ProjectDAO projectDAO;
	
	/**
	 * Find project by id
	 * 
	 * @param id
	 * @return
	 */
	public Project findByIdAndStatus(Integer id, String status) {
		Project project = projectDAO.findByIdAndStatus(id, EnumStatus.ACTIVE.getValue())
				.orElseThrow(() -> new NotFoundValueException("Not found project by Id!", "ProjectService -> findByIdAndStatus"));
		return project;
	}
}
