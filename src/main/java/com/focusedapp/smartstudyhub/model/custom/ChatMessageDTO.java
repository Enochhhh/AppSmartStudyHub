package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;

import com.focusedapp.smartstudyhub.model.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String content;
	private Integer userId;
	private String type;
	private String sender;
	private String imageUrl;
	private Long dateSent;
	
	public ChatMessageDTO(ChatMessage chatMessage) {
		this.content = chatMessage.getContent();
		this.userId = chatMessage.getUser().getId();
		this.type = chatMessage.getType();
		this.sender = chatMessage.getUser().getLastName()
				.concat(" ")
				.concat(chatMessage.getUser().getFirstName());
		this.imageUrl = chatMessage.getUser().getImageUrl();
		this.dateSent = chatMessage.getDateSent().getTime();
	}
}
