package com.focusedapp.smartstudyhub.model.chatgpt;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class ChatGptResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Choice> choices;
	private String id;
	private String object;
	private Long created;
	private String model;
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(value = Include.NON_NULL)
	public static class Choice {
		private int index;
		private Message message;
	}

}
