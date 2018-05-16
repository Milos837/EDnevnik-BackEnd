package com.example.final_project_test.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.example.final_project_test.entities.enums.EGradeType;
import com.example.final_project_test.entities.enums.EGradeValue;

@Entity
@Table(name = "grade")
public class GradeEntity {

	@Id
	@GeneratedValue
	private Integer id;

	@Column
	private EGradeValue value;

	@Column
	private EGradeType type;

	@Column
	private Date date;

	@Column
	private Boolean finalGrade;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "studentCourse")
	private StudentCourseEntity studentCourse;

	@Version
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

	public EGradeType getType() {
		return type;
	}

	public void setType(EGradeType type) {
		this.type = type;
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

	public StudentCourseEntity getStudentCourse() {
		return studentCourse;
	}

	public void setStudentCourse(StudentCourseEntity studentCourse) {
		this.studentCourse = studentCourse;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
