package com.example.final_project_test.controllers.util;

public class RESTError {

	private Integer code;

	private String message;
	
	public RESTError(Integer code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public Integer getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}

}
