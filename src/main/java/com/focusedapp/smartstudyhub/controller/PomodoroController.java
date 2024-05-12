package com.focusedapp.smartstudyhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.PomodoroDTO;
import com.focusedapp.smartstudyhub.model.custom.PomorodoGroupByDateDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.PomodoroService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user/guest/pomodoro")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class PomodoroController extends BaseController {

	@Autowired
	PomodoroService pomodoroService;
	
	/**
	 * Create Pomodoro
	 * 
	 * @param pomodoroRequest
	 * @return
	 */
	@PostMapping("/create")
	public ResponseEntity<Result<PomodoroDTO>> createPomodoro(@RequestBody PomodoroDTO pomodoroRequest) {
		Result<PomodoroDTO> result = new Result<>();
		
		PomodoroDTO pomodoroCreated = pomodoroService.createPomodoro(pomodoroRequest);
		
		result.setData(pomodoroCreated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Pomodoros
	 * 
	 * @param pomodoroRequest
	 * @return
	 */
	@GetMapping("/get")
	public ResponseEntity<Result<List<PomorodoGroupByDateDTO>>> getPomodoros(@RequestParam Integer userId) {
		Result<List<PomorodoGroupByDateDTO>> result = new Result<>();
		
		List<PomorodoGroupByDateDTO> pomodoroGetted = pomodoroService.getPomodoros(userId);
		
		result.setData(pomodoroGetted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Pomodoros
	 * 
	 * @param pomodoroRequest
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	@DeleteMapping("/delete/{pomodoroId}")
	public ResponseEntity<Result<PomodoroDTO>> deletePomodoro(@PathVariable Integer pomodoroId) {
		Result<PomodoroDTO> result = new Result<>();
		
		PomodoroDTO pomodoroDeleted= pomodoroService.deletePomodoro(pomodoroId);
		
		result.setData(pomodoroDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Completyly All Pomodoros of User Controller
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	@DeleteMapping("/delete-completely-all/{userId}")
	public ResponseEntity<Result<AllResponseTypeDTO>> deleteCompletelyAllPomodorosOfUser(@PathVariable Integer userId) {
		Result<AllResponseTypeDTO> result = new Result<>();
		
		if (userId == null || userId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Request Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		Boolean isDeleted = pomodoroService.deleteCompletelyAllPomodorosOfUserByThread(userId);
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		data.setBooleanType(isDeleted);
		data.setStringType("Deleted Successfully!");
		
		result.setData(data);
		return createResponseEntity(result);
	}
	
}
