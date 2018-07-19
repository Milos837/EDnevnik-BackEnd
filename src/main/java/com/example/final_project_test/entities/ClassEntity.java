package com.example.final_project_test.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.example.final_project_test.entities.enums.ESchoolYear;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "class")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ClassEntity {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column
	@NotNull(message = "Class number must not be null.")
	@Pattern(regexp = "^[1-9{n}]$", message = "Class number must be integer in range of [1-9].")
	private String classNumber;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Year is null or invalid. Accepted values: [FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH, EIGHTH].")
	private ESchoolYear year;
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "supervisorTeacher")
	private TeacherEntity supervisorTeacher;
	
	@OneToMany(mappedBy = "attendingClass", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<StudentEntity> students;
	
	@Column
	private Boolean deleted;
	
	@Version
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
	public String getClassNumber() {
		return classNumber;
	}
	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
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
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	

}
