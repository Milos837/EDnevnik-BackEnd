package com.example.final_project_test.entities;

import java.util.List;

import com.example.final_project_test.entities.enums.ESchoolYear;

public class ClassEntity {
	
	private Integer id;
	private String name;
	private ESchoolYear year;
	private TeacherEntity supervisorTeacher;
	private List<StudentEntity> students;
	private List<CourseEntity> courses;
	private Integer version;
	
	public ClassEntity() {
		super();
	}
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
	public ESchoolYear getYear() {
		return year;
	}
	public void setYear(ESchoolYear year) {
		this.year = year;
	}
	public TeacherEntity getSupervisorTeacher() {
		return supervisorTeacher;
	}
	public void setSupervisorTeacher(TeacherEntity supervisorTeacher) {
		this.supervisorTeacher = supervisorTeacher;
	}
	public List<StudentEntity> getStudents() {
		return students;
	}
	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}
	public List<CourseEntity> getCourses() {
		return courses;
	}
	public void setCourses(List<CourseEntity> courses) {
		this.courses = courses;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	

}
