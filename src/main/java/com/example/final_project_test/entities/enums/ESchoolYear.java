package com.example.final_project_test.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ESchoolYear {
	
	FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH, EIGHTH;
	
	@JsonCreator
	public static ESchoolYear create(String value) {
	    if(value == null) {
	        return null;
	    }
	    for(ESchoolYear v : values()) {
	        if(value.equals(v.toString())) {
	            return v;
	        }
	    }
	    return null;
	}


}
