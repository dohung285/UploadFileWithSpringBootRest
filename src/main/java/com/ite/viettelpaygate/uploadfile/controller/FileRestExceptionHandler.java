package com.ite.viettelpaygate.uploadfile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ite.viettelpaygate.uploadfile.exception.FileErrorResponse;

@ControllerAdvice
public class FileRestExceptionHandler {
	
	@ExceptionHandler
	public ResponseEntity<FileErrorResponse> handleException(Exception exc) {
		
		// create a FileErrorResponse
		FileErrorResponse error = new FileErrorResponse();
		
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		// return ResponseEntity		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	

}
