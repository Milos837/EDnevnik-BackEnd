package com.example.final_project_test.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.final_project_test.entities.dto.ParentDto;

@Component
public class ParentCustomValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return ParentDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ParentDto parent = (ParentDto) target;
		
		if(!parent.getPassword().equals(parent.getConfirmedPassword())) {
			errors.reject("400", "Passwords don't match.");
		}
		
	}

}
