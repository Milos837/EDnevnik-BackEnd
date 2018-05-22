package com.example.final_project_test.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.final_project_test.repositories.ParentRepository;

public class UniqueEmailConstraintValidator implements ConstraintValidator<UniqueEmail, String>{
	
	@Autowired
	private ParentRepository parentRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if(parentRepository.existsByEmail(value)) {
			return false;
		}
		
		return true;
	}

}
