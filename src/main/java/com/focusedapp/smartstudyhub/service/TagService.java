package com.focusedapp.smartstudyhub.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.TagDAO;
import com.focusedapp.smartstudyhub.model.Tag;
import com.focusedapp.smartstudyhub.model.custom.TagDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class TagService {

	@Autowired
	TagDAO tagDAO;
	@Autowired
	UserService userService;
	
	public TagDTO create(TagDTO tagReq) {
		
		Tag tag = Tag.builder()
				.user(userService.findByIdAndStatus(tagReq.getUserId(), EnumStatus.ACTIVE.getValue()))
				.tagName(tagReq.getTagName())
				.colorCode(tagReq.getColorCode())
				.createdDate(new Date())
				.status(EnumStatus.ACTIVE.getValue())
				.build();
		
		tag = tagDAO.save(tag);
		return new TagDTO(tag);
	}
}
