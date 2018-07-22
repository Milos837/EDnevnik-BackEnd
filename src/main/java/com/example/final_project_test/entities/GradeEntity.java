package com.example.final_project_test.entities;

import java.time.ZonedDateTime;  

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.example.final_project_test.entities.enums.EGradeType;
import com.example.final_project_test.entities.enums.EGradeValue;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "grade")
public class GradeEntity {

	@Id
	@GeneratedValue
	private Integer id;

	//@Column
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Grade value must not be null.")
	private EGradeValue value;

	//@Column
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Grade type must not be null.")
	private EGradeType type;

	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm")
	private ZonedDateTime dateUTC;

	@Column
	private Boolean finalGrade;
	
	@Column
	private Boolean deleted;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "studentTeacherCourse")
	private StudentTeacherCourseEntity studentTeacherCourse;

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

	public ZonedDateTime getDateUTC() {
		return dateUTC;
	}

	public void setDateUTC(ZonedDateTime dateUTC) {
		this.dateUTC = dateUTC;
	}

	public Boolean getFinalGrade() {
		return finalGrade;
	}

	public void setFinalGrade(Boolean finalGrade) {
		this.finalGrade = finalGrade;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public StudentTeacherCourseEntity getStudentTeacherCourse() {
		return studentTeacherCourse;
	}

	public void setStudentTeacherCourse(StudentTeacherCourseEntity studentTeacherCourse) {
		this.studentTeacherCourse = studentTeacherCourse;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
