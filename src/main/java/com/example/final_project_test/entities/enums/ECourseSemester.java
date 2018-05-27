package com.example.final_project_test.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ECourseSemester {
	
	FALL, SPRING;
	
	@JsonCreator
	public static ECourseSemester create(String value) {
	    if(value == null) {
	        return null;
	    }
	    for(ECourseSemester v : values()) {
	        if(value.equals(v.toString())) {
	            return v;
	        }
	    }
	    return null;
	}

}
