package com.example.final_project_test.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "parent")
public class ParentEntity extends UserEntity{
	
	@Column
	@NotNull(message = "Email must not be null.")
	@Email(message = "Email not formated correctly.")
	private String email;
	
	@OneToMany(mappedBy = "parent", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
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
