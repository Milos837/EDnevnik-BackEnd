package com.example.final_project_test.entities;

import java.util.List; 

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.example.final_project_test.entities.enums.ESchoolYear;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "class")
public class ClassEntity {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column
	private String name;
	
	@Column
	private ESchoolYear year;
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "supervisorTeacher")
	private TeacherEntity supervisorTeacher;
	
	@OneToMany(mappedBy = "attendingClass", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<StudentEntity> students;
	
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
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	

}
