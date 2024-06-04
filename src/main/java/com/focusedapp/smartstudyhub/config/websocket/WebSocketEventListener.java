package com.focusedapp.smartstudyhub.config.websocket;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.focusedapp.smartstudyhub.model.ChatMessage;
import com.focusedapp.smartstudyhub.model.custom.ChatMessageDTO;
import com.focusedapp.smartstudyhub.service.ChatMessageService;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.enumerate.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
	
	@Autowired SimpMessageSendingOperations messageTemplate;
	@Autowired UserService userService;
	@Autowired ChatMessageService chatMessageService;

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String userName = (String) headerAccessor.getSessionAttributes().get("username");
		Integer userId = (Integer) headerAccessor.getSessionAttributes().get("userid");
		if (userId != null && StringUtils.isNotBlank(userName)) {
			log.info("User disconnected: {}", userName);
			var chatMessageDto = ChatMessageDTO.builder()
					.content(userName.concat(" left group chat!"))
					.type(MessageType.LEAVE.getValue())
					.sender(userName)
					.build();
			ChatMessage chatMessageSave = ChatMessage.builder()
					.user(userService.findById(userId))
					.content(userName.concat(" left group chat!"))
					.dateSent(new Date())
					.type(MessageType.LEAVE.getValue())
					.build();
			chatMessageService.persistent(chatMessageSave);
			messageTemplate.convertAndSend("/topic/public", chatMessageDto);
		}
		
	}
	
}
