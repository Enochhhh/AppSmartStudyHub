package com.focusedapp.smartstudyhub.service;

import java.util.Date;
import java.util.List;

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
	
	/**
	 * Find Tags By Ids List
	 * 
	 * @param tagIds
	 * @return
	 */
	public List<Tag> findByIds(List<Integer> tagIds) {
		return tagDAO.findByIds(tagIds);
	}
	
	/**
	 * Create Tag
	 * @param tagReq
	 * @return
	 */
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
