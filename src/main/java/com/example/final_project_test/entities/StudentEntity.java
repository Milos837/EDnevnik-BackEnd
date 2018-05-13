package com.example.final_project_test.entities;

import java.util.List;

public class StudentEntity extends UserEntity{
	
	private ClassEntity attendingClass;
	private List<GradeEntity> grades;
	private ParentEntity parent;

	public StudentEntity() {
		super();
	}

	public ClassEntity getAttendingClass() {
		return attendingClass;
	}

	public void setAttendingClass(ClassEntity attendingClass) {
		this.attendingClass = attendingClass;
	}

	public List<GradeEntity> getGrades() {
		return grades;
	}

	public void setGrades(List<GradeEntity> grades) {
		this.grades = grades;
	}

	public ParentEntity getParent() {
		return parent;
	}

	public void setParent(ParentEntity parent) {
		this.parent = parent;
	}

}
