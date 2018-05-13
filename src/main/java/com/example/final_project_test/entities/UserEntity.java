package com.example.final_project_test.entities;

import com.example.final_project_test.entities.enums.EUserRole;

public class UserEntity {
	
	private Integer id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private EUserRole role;
	private Integer version;
	
	public UserEntity() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public EUserRole getRole() {
		return role;
	}
	public void setRole(EUserRole role) {
		this.role = role;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}

}
