package com.example.final_project_test.entities.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.example.final_project_test.entities.enums.ESchoolYear;

public class ClassDto {
	
	@NotNull(message = "Class number must not be null.")
	@Pattern(regexp = "^[1-9{n}]$", message = "Class number must be integer in range of [1-9].")
	private String classNumber;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Year is null or invalid. Accepted values: [FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH, EIGHTH].")
	private ESchoolYear year;

	public ClassDto() {
		super();
	}

	public String getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}

	public ESchoolYear getYear() {
		return year;
	}

	public void setYear(ESchoolYear year) {
		this.year = year;
	}

}
