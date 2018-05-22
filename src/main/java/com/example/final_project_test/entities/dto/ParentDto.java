package com.example.final_project_test.entities.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.final_project_test.validation.UniqueEmail;
import com.example.final_project_test.validation.UniqueUsername;

public class ParentDto {
	
	@UniqueUsername
	@NotNull(message = "Username must not be null")
	@Size(min = 5, max = 15, message = "Username must be between {min} and {max} characters.")
	private String username;
	
	@NotNull(message = "Password must not be null")
	@Size(min = 5, max = 15, message = "Password must be between {min} and {max} characters.")
	private String password;
	
	@NotNull
	private String confirmedPassword;
	
	@NotNull(message = "First name must not be null.")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters.")
	private String firstName;
	
	@NotNull(message = "Last name must not be null")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters.")
	private String lastName;
	
	@UniqueEmail
	@NotNull(message = "Email must not be null.")
	@Email(message = "Email not formated correctly.")
	private String email;

	public ParentDto() {
		super();
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

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
