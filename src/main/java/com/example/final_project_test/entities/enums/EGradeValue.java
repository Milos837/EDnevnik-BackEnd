package com.example.final_project_test.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EGradeValue {
	
	INSUFFICIENT, SUFFICIENT, GOOD, VERY_GOOD, EXCELLENT;
	
	@JsonCreator
	public static EGradeValue create(String value) {
	    if(value == null) {
	        return null;
	    }
	    for(EGradeValue v : values()) {
	        if(value.equals(v.toString())) {
	            return v;
	        }
	    }
	    return null;
	}

}
