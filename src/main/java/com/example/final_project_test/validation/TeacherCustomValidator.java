package com.example.final_project_test.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.final_project_test.entities.dto.TeacherDto;

@Component
public class TeacherCustomValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return TeacherDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TeacherDto teacher = (TeacherDto) target;
		
		if(!teacher.getPassword().equals(teacher.getConfirmedPassword())) {
			errors.reject("400", "Passwords don't match.");
		}
		
	}

}
