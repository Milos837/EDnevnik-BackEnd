package com.example.final_project_test.entities;

import java.util.Date;

import com.example.final_project_test.entities.enums.EGradeValue;

public class GradeEntity {
	
	private Integer id;
	private EGradeValue value;
	private Date date;
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
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	

}
