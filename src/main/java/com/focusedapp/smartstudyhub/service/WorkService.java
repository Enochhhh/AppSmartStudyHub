package com.focusedapp.smartstudyhub.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.WorkDAO;
import com.focusedapp.smartstudyhub.model.Folder;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.model.custom.ProjectDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class WorkService {
	
	@Autowired
	WorkDAO workDAO;
	
	public WorkDTO createWork(WorkDTO dataCreate) {

//		Optional<Folder> folderOpt = workDAO.findByIdAndStatus(dataCreate.getFolderId(), EnumStatus.ACTIVE.getValue());
//		Folder folder = null;
//		if (!folderOpt.isEmpty()) {
//			folder = folderOpt.get();
//		}
//		
//		Work work = WorkDTO.builder()
//				.user(userService.findByIdAndStatus(dataCreate.getUserId(), EnumStatus.ACTIVE.getValue()))
//				.folder(folder)
//				.projectName(dataCreate.getProjectName())
//				.colorCode(dataCreate.getColorCode())
//				.iconUrl(dataCreate.getIconUrl())
//				.status(EnumStatus.ACTIVE.getValue()).build();
//		work = workDAO.save(work);
//
//		return new WorkDTO(work);
		return null;
	}

}
