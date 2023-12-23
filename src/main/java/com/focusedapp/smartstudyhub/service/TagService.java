package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.TagDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.Tag;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.TagDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class TagService {

	@Autowired
	TagDAO tagDAO;
	@Autowired
	UserService userService;
	@Autowired
	WorkService workService;
	
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
	 * Find Tag by Id
	 * 
	 * @param tagId
	 * @return
	 */
	public Tag findById(Integer tagId) {
		return tagDAO.findById(tagId)
					.orElseThrow(() -> new NotFoundValueException("Not Found Tag by Id", "TagService -> findById"));
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
	
	/**
	 * Update Tag
	 * 
	 * @param tagReq
	 * @return
	 */
	public TagDTO update(TagDTO tagReq) {
		
		Tag tag = findById(tagReq.getId());
		
		tag.setTagName(tagReq.getTagName());
		tag.setStatus(tagReq.getStatus());
		tag.setColorCode(tagReq.getColorCode());
		
		tag = tagDAO.save(tag);
		
		return new TagDTO(tag);
	}
	
	/**
	 * Delete Tag by Id
	 * 
	 * @param tagId
	 * @return
	 */
	public TagDTO delete(Integer tagId) {
		
		Tag tag = findById(tagId);
		
		tagDAO.delete(tag);
		
		return new TagDTO(tag);
	}
	
	/**
	 * Find tag by User Id and Status
	 * 
	 * @return
	 */
	public List<Tag> findByUserIdAndStatus(Integer userId, String status) {
		
		return tagDAO.findByUserIdAndStatus(userId, status);
	}
	
	/**
	 * Searh Tag By Name
	 * 
	 * @param keySearch
	 * @return
	 */
	public List<TagDTO> searchByName(String keySearch, Integer userId) {
		List<Tag> tags = new ArrayList<>();
		if (StringUtils.isEmpty(keySearch)) {
			tags = tagDAO.findByUserIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		} else {
			tags = tagDAO.findByTagNameContainingAndUserIdAndStatus(keySearch, userId, EnumStatus.ACTIVE.getValue());
		}
		return tags.stream()
					.map(t -> new TagDTO(t))
					.collect(Collectors.toList());
	}
	
	/**
	 * Delete All Tags of User
	 * 
	 * @param user
	 */
	public void deleteAllTagsOfUser(User user) {
		tagDAO.deleteByUser(user);
	}
}
