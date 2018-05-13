package com.example.final_project_test.entities;

import java.util.List;

public class ParentEntity extends UserEntity{
	
	private String email;
	private List<StudentEntity> children;

	public ParentEntity() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<StudentEntity> getChildren() {
		return children;
	}

	public void setChildren(List<StudentEntity> children) {
		this.children = children;
	}
	
	
}
