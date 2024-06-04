package com.focusedapp.smartstudyhub.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.ChatMessage;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.ChatMessageDTO;
import com.focusedapp.smartstudyhub.service.ChatMessageService;
import com.focusedapp.smartstudyhub.service.ThreadService;
import com.focusedapp.smartstudyhub.service.UserService;


@RestController
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class ChatController {
	
	@Autowired UserService userService;
	@Autowired ChatMessageService chatMessageService;
	@Autowired ThreadService threadService;

	/**
	 * Send Message to group chat
	 * Note: Client only send field userId, type and content
	 * 
	 * @param chatMessage
	 * @return
	 */
	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessage) {
		User user = userService.findById(chatMessage.getUserId());
		String userName = user.getLastName().concat(" ").concat(user.getFirstName()).trim();
		chatMessage.setDateSent(new Date().getTime());
		chatMessage.setImageUrl(user.getImageUrl());
		chatMessage.setSender(userName);
		
		ChatMessage chaMessageSave = ChatMessage.builder()
				.content(chatMessage.getContent())
				.dateSent(new Date(chatMessage.getDateSent()))
				.type(chatMessage.getType())
				.user(user)
				.build();
		chatMessageService.persistent(chaMessageSave);
		return chatMessage;
	}
	
	/**
	 * Add User to Group chat
	 * Note: Client only send field userId and type
	 * 
	 * @param chatMessage
	 * @param headerAccessor
	 * @return
	 */
	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public ChatMessageDTO addUser(@Payload ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		User user = userService.findById(chatMessage.getUserId());
		String userName = user.getLastName().concat(" ").concat(user.getFirstName()).trim();
		headerAccessor.getSessionAttributes().put("userid", user.getId());
		headerAccessor.getSessionAttributes().put("username", userName);
		chatMessage.setDateSent(new Date().getTime());
		chatMessage.setSender(userName);
		chatMessage.setImageUrl(user.getImageUrl());
		chatMessage.setContent(userName.concat(" joined group chat!"));
		
		ChatMessage chaMessageSave = ChatMessage.builder()
				.content(chatMessage.getContent())
				.dateSent(new Date(chatMessage.getDateSent()))
				.type(chatMessage.getType())
				.user(user)
				.build();
		chatMessageService.persistent(chaMessageSave);
		return chatMessage;
	}

}
