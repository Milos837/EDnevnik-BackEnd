package com.example.final_project_test.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "teacher")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TeacherEntity extends UserEntity {

	@OneToOne(mappedBy = "supervisorTeacher", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private ClassEntity supervisesClass;
	
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<TeacherCourseEntity> teacherCourse;

	public TeacherEntity() {
		super();
	}

	public ClassEntity getSupervisesClass() {
		return supervisesClass;
	}

	public void setSupervisesClass(ClassEntity supervisesClass) {
		this.supervisesClass = supervisesClass;
	}

	public List<TeacherCourseEntity> getTeacherCourse() {
		return teacherCourse;
	}

	public void setTeacherCourse(List<TeacherCourseEntity> teacherCourse) {
		this.teacherCourse = teacherCourse;
	}

}
