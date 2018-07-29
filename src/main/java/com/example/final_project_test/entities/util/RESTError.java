package com.example.final_project_test.entities.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RESTError {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private Integer code;

	private String message;
	
	public RESTError(Integer code, String message) {
		super();
		this.code = code;
		this.message = message;
		this.logger.error("Error code " + this.code + ", message: " + this.message);
	}
	public Integer getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}

}
