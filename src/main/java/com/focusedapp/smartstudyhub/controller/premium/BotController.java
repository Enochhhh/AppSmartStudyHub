package com.focusedapp.smartstudyhub.controller.premium;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.model.chatgpt.ChatGptRequestDTO;
import com.focusedapp.smartstudyhub.model.chatgpt.ChatGptResponseDTO;
import com.focusedapp.smartstudyhub.model.chatgpt.Message;
import com.focusedapp.smartstudyhub.model.custom.DeviceDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/mobile/v1/user/premium/bot")
public class BotController extends BaseController {
	
	@Value("${openai.model}")
	String model;
	
	@Value("${openai.api.url.chat}")
	String apiUrl;
	
	@Autowired
	private RestTemplate template;

	/**
	 * Send message to bot chat
	 * 
	 * @param message
	 * @param request
	 * @param session
	 * @return
	 */
	@PostMapping("/chat")
	public ResponseEntity<Result<ChatGptResponseDTO>> chat(@RequestBody Message message, HttpServletRequest request, HttpSession session) {
		Result<ChatGptResponseDTO> result = new Result<>();
		
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
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		result.setData(response);
		return createResponseEntity(result);
	}
	
	/**
	 * Get Messages sent in session
	 * 
	 * @param session
	 * @return
	 */
	@GetMapping("/get-messages")
	public ResponseEntity<Result<List<Message>>> getMessages(HttpSession session) {
		Result<List<Message>> result = new Result<>();
		
		@SuppressWarnings("unchecked")
		List<Message> messages = (List<Message>) session.getAttribute("MESSAGES_SESSION");

		if (messages == null) {
			messages = new ArrayList<>();
		}
		result.setData(messages);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
}
