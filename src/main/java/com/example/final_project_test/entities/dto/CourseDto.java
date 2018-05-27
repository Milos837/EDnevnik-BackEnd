package com.example.final_project_test.entities.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.final_project_test.entities.enums.ECourseSemester;
import com.example.final_project_test.entities.enums.ESchoolYear;

public class CourseDto {
	
	@NotNull(message = "Course name must not be null.")
	@Size(min = 5, max = 30, message = "Course name must be between {min} and {max} characters.")
	@Pattern(regexp = "^[a-zA-Z0-9\\s,]*$", message = "Invalid course name.")
	private String name;
	
	@NotNull(message = "Weekly hours must not be null.")
	@Min(value = 0, message = "Weekly hours cannot be less than zero.")
	@Max(value = 40, message = "Weekly hours cannot be above 40.")
	private Integer weeklyHours;
	
	@NotNull(message = "Year is null or invalid. Accepted values: [FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH, EIGHTH].")
	@Enumerated(EnumType.STRING)
	private ESchoolYear year;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Semester is null or invalid. Accepted values: [FALL, SPRING].")
	private ECourseSemester semester;

	public CourseDto() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeeklyHours() {
		return weeklyHours;
	}

	public void setWeeklyHours(Integer weeklyHours) {
		this.weeklyHours = weeklyHours;
	}

	public ESchoolYear getYear() {
		return year;
	}

	public void setYear(ESchoolYear year) {
		this.year = year;
	}

	public ECourseSemester getSemester() {
		return semester;
	}

	public void setSemester(ECourseSemester semester) {
		this.semester = semester;
	}

}
