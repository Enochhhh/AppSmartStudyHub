package com.focusedapp.smartstudyhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.custom.ChatMessageDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.ChatMessageService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@RestController
@RequestMapping("/mobile/v1/user/guest/chatmessage")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class ChatMessageController extends BaseController {

	@Autowired ChatMessageService chatMessageService;
	
	/**
	 * Get Messages in group chat
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/get")
	public ResponseEntity<Result<List<ChatMessageDTO>>> getMessagesInGroup(Integer page, Integer size) {
		Result<List<ChatMessageDTO>> result = new Result<>();
		List<ChatMessageDTO> messages = chatMessageService.getAllMessages(page, size);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (CollectionUtils.isEmpty(messages)) {
			result.getMeta().setStatusCode(StatusCode.GET_DETAIL_EVENT_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.GET_DETAIL_EVENT_FAILURE.getMessage());
		}
		result.setData(messages);
		return createResponseEntity(result);
	}
}
