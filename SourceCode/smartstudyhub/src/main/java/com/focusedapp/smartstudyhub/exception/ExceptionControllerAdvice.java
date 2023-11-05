package com.focusedapp.smartstudyhub.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

import net.minidev.json.JSONObject;

@ControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Result<JSONObject>> exceptionSystem(Exception exception) {
		
		Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
		logger.error(exception.getMessage());
		
		Result<JSONObject> result = new Result<>();
		result.getMeta().setStatusCode(StatusCode.SYSTEM_FAILURE.getCode());
		result.getMeta().setMessage(StatusCode.SYSTEM_FAILURE.getMessage());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "System Failure - Unknown Error");
		result.setData(jsonObject);
		result.setLogInfo(exception.getMessage());
		
		return new ResponseEntity<Result<JSONObject>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = NotFoundValueException.class)
	public ResponseEntity<Result<JSONObject>> notFoundValueException(NotFoundValueException exception) {
			
		Result<JSONObject> result = new Result<>();
		result.getMeta().setStatusCode(StatusCode.DATA_NOT_FOUND.getCode());
		result.getMeta().setMessage(StatusCode.DATA_NOT_FOUND.getMessage());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "Data not found");
		result.setData(jsonObject);
		result.setLogInfo(exception.getMessage());
		result.setUrl(exception.getPath());
		
		return new ResponseEntity<Result<JSONObject>>(result, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = ValueExistedException.class)
	public ResponseEntity<Result<JSONObject>> valueExistedException(ValueExistedException exception) {
			
		Result<JSONObject> result = new Result<>();
		result.getMeta().setStatusCode(StatusCode.DATA_EXISTED.getCode());
		result.getMeta().setMessage(StatusCode.DATA_EXISTED.getMessage());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "Data Existed");
		result.setData(jsonObject);
		result.setLogInfo(exception.getMessage());
		result.setUrl(exception.getPath());
		
		return new ResponseEntity<Result<JSONObject>>(result, HttpStatus.FOUND);
	}
	
}