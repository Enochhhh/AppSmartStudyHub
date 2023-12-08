package com.focusedapp.smartstudyhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.custom.PomodoroDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.PomodoroService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user/guest/pomodoro")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class PomodoroController extends BaseController {

	@Autowired
	PomodoroService pomodoroService;
	
	@PostMapping("/create")
	public ResponseEntity<Result<PomodoroDTO>> createPomodoro(@RequestBody PomodoroDTO pomodoroRequest) {
		Result<PomodoroDTO> result = new Result<>();
		
		PomodoroDTO pomodoroCreated = pomodoroService.createPomodoro(pomodoroRequest);
		
		result.setData(pomodoroCreated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
}
