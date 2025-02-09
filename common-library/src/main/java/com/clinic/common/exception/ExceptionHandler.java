package com.clinic.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleNotFoundException(EntityNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}
}
