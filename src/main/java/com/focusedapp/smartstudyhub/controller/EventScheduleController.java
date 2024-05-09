package com.focusedapp.smartstudyhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.EventScheduleDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.TimeLineEventDTO;
import com.focusedapp.smartstudyhub.service.EventScheduleService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@RestController
@RequestMapping("/mobile/v1/user/guest/event")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class EventScheduleController extends BaseController {

	@Autowired EventScheduleService eventScheduleService;
	
	/**
	 * Create Event
	 * 
	 * @param eventScheduleDTO
	 * @return
	 */
	@PostMapping("/create")
	public ResponseEntity<Result<EventScheduleDTO>> createEvent(@RequestBody EventScheduleDTO eventScheduleDTO) {
		Result<EventScheduleDTO> result = new Result<>();
		if (eventScheduleDTO == null || eventScheduleDTO.getStartTime() > eventScheduleDTO.getEndTime()) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		EventScheduleDTO event = eventScheduleService.createEvent(eventScheduleDTO);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (event == null) {
			result.getMeta().setStatusCode(StatusCode.CREATE_EVENT_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.CREATE_EVENT_FAILURE.getMessage());
		}
		result.setData(event);
		return createResponseEntity(result);
	}
	
	/**
	 * Get Detail Event by id API
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<Result<EventScheduleDTO>> getDetailEvent(@PathVariable Integer id) {
		Result<EventScheduleDTO> result = new Result<>();
		if (id == null || id < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		EventScheduleDTO event = eventScheduleService.getById(id);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (event == null) {
			result.getMeta().setStatusCode(StatusCode.GET_DETAIL_EVENT_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.GET_DETAIL_EVENT_FAILURE.getMessage());
		}
		result.setData(event);
		return createResponseEntity(result);
	}
	
	/**
	 * Update Event API
	 * 
	 * @param eventData
	 * @return
	 */
	@PutMapping("/update")
	public ResponseEntity<Result<EventScheduleDTO>> updateEvent(@RequestBody EventScheduleDTO eventData) {
		Result<EventScheduleDTO> result = new Result<>();
		if (eventData == null || eventData.getId() == null || eventData.getId() < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		EventScheduleDTO event = eventScheduleService.update(eventData);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (event == null) {
			result.getMeta().setStatusCode(StatusCode.GET_DETAIL_EVENT_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.GET_DETAIL_EVENT_FAILURE.getMessage());
		}
		result.setData(event);
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Event API
	 * 
	 * @param eventData
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Result<AllResponseTypeDTO>> deleteEvent(@PathVariable Integer id) {
		Result<AllResponseTypeDTO> result = new Result<>();
		if (id == null || id < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		Boolean isDeleted = eventScheduleService.delete(id);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		AllResponseTypeDTO data = AllResponseTypeDTO.builder()
				.booleanType(isDeleted)
				.stringType("Deleted Event Successfully!")
				.build();
		if (!isDeleted) {
			result.getMeta().setStatusCode(StatusCode.DELETE_EVENT_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.DELETE_EVENT_FAILURE.getMessage());
			data.setStringType("Not found Event for deleting!");
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Get Time Line Event
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@GetMapping("/get-time-line")
	public ResponseEntity<Result<List<TimeLineEventDTO>>> getTimeLineEvent(@RequestParam Integer userId, 
			@RequestParam Long startDate, @RequestParam Long endDate) {
		Result<List<TimeLineEventDTO>> result = new Result<>();
		if (startDate == null || startDate < 0 
				|| endDate == null || endDate < 0) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		List<TimeLineEventDTO> timeLineEvents = eventScheduleService.getTimeLineEvent(startDate, endDate, userId);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (CollectionUtils.isEmpty(timeLineEvents)) {
			result.getMeta().setStatusCode(StatusCode.GET_TIME_LINE_EVENT_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.GET_TIME_LINE_EVENT_FAILURE.getMessage());
		}
		result.setData(timeLineEvents);
		return createResponseEntity(result);
	}
	
}
