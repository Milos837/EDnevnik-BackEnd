package com.example.final_project_test.entities;

import java.util.List;

import com.example.final_project_test.entities.enums.ECourseSemester;
import com.example.final_project_test.entities.enums.ESchoolYear;

public class CourseEntity {
	
	private Integer id;
	private String name;
	private Integer weeklyHours;
	private ESchoolYear year;
	private ECourseSemester semester;
	private List<TeacherEntity> teachers;
	private List<ClassEntity> classes;
	private List<GradeEntity> grades;
	private Integer version;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public List<TeacherEntity> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherEntity> teachers) {
		this.teachers = teachers;
	}

	public List<ClassEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassEntity> classes) {
		this.classes = classes;
	}

	public List<GradeEntity> getGrades() {
		return grades;
	}

	public void setGrades(List<GradeEntity> grades) {
		this.grades = grades;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public CourseEntity() {
		super();
	}
	

}
