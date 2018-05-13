package com.example.final_project_test.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "teacher")
public class TeacherEntity extends UserEntity {

	@OneToOne(mappedBy = "supervisorTeacher", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private ClassEntity supervisesClass;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "teacher_course", joinColumns =
		{@JoinColumn(name = "teacher_id", nullable = false, updatable = false) },
		inverseJoinColumns = { @JoinColumn(name = "course_id",
		nullable = false, updatable = false) })
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
