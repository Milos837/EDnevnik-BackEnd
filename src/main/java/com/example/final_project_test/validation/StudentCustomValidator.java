package com.example.final_project_test.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.final_project_test.entities.dto.StudentDto;

@Component
public class StudentCustomValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return StudentDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		StudentDto student = (StudentDto) target;
		
		if(!student.getPassword().equals(student.getConfirmedPassword())) {
			errors.reject("400", "Passwords don't match.");
		}
		
	}

}
