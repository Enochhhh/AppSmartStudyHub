package com.focusedapp.smartstudyhub.controller.premium;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.focusedapp.smartstudyhub.model.chatgpt.ChatGptRequestDTO;
import com.focusedapp.smartstudyhub.model.chatgpt.ChatGptResponseDTO;
import com.focusedapp.smartstudyhub.model.chatgpt.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/mobile/v1/user/premium/bot")
public class BotController {
	
	@Value("${openai.model}")
	String model;
	
	@Value("${openai.api.url.chat}")
	String apiUrl;
	
	@Autowired
	private RestTemplate template;

	@PostMapping("/chat")
	public ChatGptResponseDTO chat(@RequestBody Message message, HttpServletRequest request, HttpSession session) {
		@SuppressWarnings("unchecked")
		List<Message> messages = (List<Message>) session.getAttribute("MESSAGES_SESSION");

		if (messages == null) {
			messages = new ArrayList<>();
		}

		messages.add(message);
		ChatGptRequestDTO chatGptRequestDTO = new ChatGptRequestDTO(model, messages);
		ChatGptResponseDTO response = template.postForObject(apiUrl, chatGptRequestDTO, ChatGptResponseDTO.class);	
		if (response != null) {
			messages.add(response.getChoices().get(0).getMessage());
		}
		session.setAttribute("MESSAGES_SESSION", messages);
		return response;
	}
}
