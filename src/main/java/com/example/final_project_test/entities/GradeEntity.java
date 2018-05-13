package com.example.final_project_test.entities;

import java.util.Date;

import com.example.final_project_test.entities.enums.EGradeValue;

public class GradeEntity {
	
	private Integer id;
	private EGradeValue value;
	private Date date;
	private Boolean finalGrade;
	private StudentEntity student;
	private CourseEntity course;
	private Integer version;
	
	public GradeEntity() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public EGradeValue getValue() {
		return value;
	}
	public void setValue(EGradeValue value) {
		this.value = value;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Boolean getFinalGrade() {
		return finalGrade;
	}
	public void setFinalGrade(Boolean finalGrade) {
		this.finalGrade = finalGrade;
	}
	public StudentEntity getStudent() {
		return student;
	}
	public void setStudent(StudentEntity student) {
		this.student = student;
	}
	public CourseEntity getCourse() {
		return course;
	}
	public void setCourse(CourseEntity course) {
		this.course = course;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	

}
