package com.example.final_project_test.entities;

import java.util.List;

public class TeacherEntity extends UserEntity{
	
	private ClassEntity supervisesClass;
	private List<CourseEntity> courses;

	public TeacherEntity() {
		super();
	}

	public ClassEntity getSupervisesClass() {
		return supervisesClass;
	}

	public void setSupervisesClass(ClassEntity supervisesClass) {
		this.supervisesClass = supervisesClass;
	}

	public List<CourseEntity> getCourses() {
		return courses;
	}

	public void setCourses(List<CourseEntity> courses) {
		this.courses = courses;
	}
	

}
