package com.example.final_project_test.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EGradeType {
	
	TEST, ESSAY, ORAL, FINAL;
	
	@JsonCreator
	public static EGradeType create(String value) {
	    if(value == null) {
	        return null;
	    }
	    for(EGradeType v : values()) {
	        if(value.equals(v.toString())) {
	            return v;
	        }
	    }
	    return null;
	}

}
