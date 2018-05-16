package com.example.final_project_test.entities;
 

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "student")
public class StudentEntity extends UserEntity{
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "attendingClass")
	private ClassEntity attendingClass;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	private ParentEntity parent;
	
	@OneToMany(mappedBy = "student", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<StudentCourseEntity> studentCourse;

	public StudentEntity() {
		super();
	}

	public ClassEntity getAttendingClass() {
		return attendingClass;
	}

	public void setAttendingClass(ClassEntity attendingClass) {
		this.attendingClass = attendingClass;
	}

	public ParentEntity getParent() {
		return parent;
	}

	public void setParent(ParentEntity parent) {
		this.parent = parent;
	}

	public List<StudentCourseEntity> getStudentCourse() {
		return studentCourse;
	}

	public void setStudentCourse(List<StudentCourseEntity> studentCourse) {
		this.studentCourse = studentCourse;
	}

}
