package com.focusedapp.smartstudyhub.exception;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.focusedapp.smartstudyhub.dao.ErrorLogDAO;
import com.focusedapp.smartstudyhub.model.ErrorLog;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

import jakarta.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;

@ControllerAdvice
public class ExceptionControllerAdvice {
	
	@Autowired
	ErrorLogDAO errorLogDAO;
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Result<JSONObject>> exceptionSystem(Exception exception, HttpServletRequest req) {
		
		Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
		logger.error(exception.getMessage());
		exception.printStackTrace();
		
		Result<JSONObject> result = new Result<>();
		result.getMeta().setStatusCode(StatusCode.SYSTEM_FAILURE.getCode());
		result.getMeta().setMessage(StatusCode.SYSTEM_FAILURE.getMessage());
		
		ErrorLog errorLog = ErrorLog.builder()
				.className(exception.getClass().getCanonicalName())
				.error(exception.getClass().getSimpleName())
				.message(exception.getMessage())
				.path(req.getRequestURI())
				.stackTrace(Arrays.stream(exception.getStackTrace())
		                .map(s->s.toString())
		                .collect(Collectors.joining("\n")))
				.createdDate(new Date()).build();
		errorLog = errorLogDAO.save(errorLog);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "System Failure - Unknown Error");
		jsonObject.put("logId", errorLog.getId());
		result.setData(jsonObject);
		
		return new ResponseEntity<Result<JSONObject>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	} 
	
	@ExceptionHandler(value = InternalAuthenticationServiceException.class)
	public ResponseEntity<Result<JSONObject>> internalAuthenticationServiceException(InternalAuthenticationServiceException exception) {
		
		Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
		logger.error(exception.getMessage());
		
		Result<JSONObject> result = new Result<>();
		result.getMeta().setStatusCode(StatusCode.AUTHENTICATED_FAILURE.getCode());
		result.getMeta().setMessage(StatusCode.AUTHENTICATED_FAILURE.getMessage());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "Authenticated Failure, please try again");
		result.setData(jsonObject);
		result.setLogInfo(exception.getMessage());
		
		return new ResponseEntity<Result<JSONObject>>(result, HttpStatus.FORBIDDEN);
	} 
	
	@ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<Result<JSONObject>> badCredentialsException(BadCredentialsException exception) {
		
		Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
		logger.error(exception.getMessage());
		
		Result<JSONObject> result = new Result<>();
		result.getMeta().setStatusCode(StatusCode.AUTHENTICATED_FAILURE.getCode());
		result.getMeta().setMessage(StatusCode.AUTHENTICATED_FAILURE.getMessage());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "Authenticated Failure, please try again");
		result.setData(jsonObject);
		result.setLogInfo(exception.getMessage());
		
		return new ResponseEntity<Result<JSONObject>>(result, HttpStatus.FORBIDDEN);
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
	
	@ExceptionHandler(value = AccountBannedException.class)
	public ResponseEntity<Result<JSONObject>> accountBannedException(AccountBannedException exception) {
			
		Result<JSONObject> result = new Result<>();
		result.getMeta().setStatusCode(StatusCode.ACCOUNT_BANNED.getCode());
		result.getMeta().setMessage(StatusCode.ACCOUNT_BANNED.getMessage());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "Account was banned");
		result.setData(jsonObject);
		result.setLogInfo(exception.getMessage());
		result.setUrl(exception.getPath());
		
		return new ResponseEntity<Result<JSONObject>>(result, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(value = AccountDeletedException.class)
	public ResponseEntity<Result<JSONObject>> accountDeletedException(AccountDeletedException exception) {
			
		Result<JSONObject> result = new Result<>();
		result.getMeta().setStatusCode(StatusCode.ACCOUNT_DELETED.getCode());
		result.getMeta().setMessage(StatusCode.ACCOUNT_DELETED.getMessage());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "Account was deleted");
		result.setData(jsonObject);
		result.setLogInfo(exception.getMessage());
		result.setUrl(exception.getPath());
		
		return new ResponseEntity<Result<JSONObject>>(result, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(value = OTPCodeInvalidException.class)
	public ResponseEntity<Result<JSONObject>> otpCodeInvalidException(OTPCodeInvalidException exception) {
			
		Result<JSONObject> result = new Result<>();
		result.getMeta().setStatusCode(StatusCode.OTP_CODE_INVALID.getCode());
		result.getMeta().setMessage(StatusCode.OTP_CODE_INVALID.getMessage());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "OTP Code Invalid Or Expired");
		result.setData(jsonObject);
		result.setLogInfo(exception.getMessage());
		result.setUrl(exception.getPath());
		
		return new ResponseEntity<Result<JSONObject>>(result, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(value = ParseException.class)
	public ResponseEntity<Result<JSONObject>> parseException(ParseException exception) {
			
		Result<JSONObject> result = new Result<>();
		result.getMeta().setStatusCode(StatusCode.PARSE_ERROR.getCode());
		result.getMeta().setMessage(StatusCode.PARSE_ERROR.getMessage());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "Parse Type Error!");
		result.setData(jsonObject);
		result.setLogInfo(exception.getMessage());
		result.setUrl(exception.getPath());
		
		return new ResponseEntity<Result<JSONObject>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = NoRightToPerformException.class)
	public ResponseEntity<Result<JSONObject>> noRightToPerformException(NoRightToPerformException exception) {
			
		Result<JSONObject> result = new Result<>();
		result.getMeta().setStatusCode(StatusCode.UNAUTHORIZED.getCode());
		result.getMeta().setMessage(StatusCode.UNAUTHORIZED.getMessage());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "No right to perform this action!");
		result.setData(jsonObject);
		result.setLogInfo(exception.getMessage());
		result.setUrl(exception.getPath());
		
		return new ResponseEntity<Result<JSONObject>>(result, HttpStatus.UNAUTHORIZED);
	}
	
}
