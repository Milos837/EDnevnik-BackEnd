package com.example.final_project_test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleAll(HttpMessageNotReadableException e) {
        return new ResponseEntity<String>(e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
    }

}
