package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.ChatMessageDAO;
import com.focusedapp.smartstudyhub.model.ChatMessage;
import com.focusedapp.smartstudyhub.model.custom.ChatMessageDTO;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@Service
public class ChatMessageService {

	@Autowired ChatMessageDAO chatMessageDAO;
	
	public ChatMessage persistent(ChatMessage chatMessage) {
		return chatMessageDAO.save(chatMessage);
	}
	
	/**
	 * Get messages in group chat
	 * 
	 * @param pageable
	 * @return
	 */
	public List<ChatMessageDTO> getAllMessages(Integer page, Integer size) {
		Pageable pageable =  PageRequest.of(page, size, Sort.by("dateSent").descending());
		List<ChatMessage> messages = chatMessageDAO.findAll(pageable).toList();
		if (CollectionUtils.isEmpty(messages)) {
			return new ArrayList<>();
		}
		return messages.stream()
				.map(m -> new ChatMessageDTO(m))
				.collect(Collectors.toList());
	}
}
