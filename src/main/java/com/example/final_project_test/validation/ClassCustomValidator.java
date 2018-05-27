package com.example.final_project_test.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.final_project_test.entities.dto.ClassDto;
import com.example.final_project_test.repositories.ClassRepository;

@Component
public class ClassCustomValidator implements Validator{
	
	@Autowired
	private ClassRepository classRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return ClassDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ClassDto classDto = (ClassDto) target;
		
		if(classRepository.existsByClassNumberAndYear(classDto.getClassNumber(), classDto.getYear())) {
			errors.reject("400", "Class year-number combination is already in use.");
		}
		
	}
	
	

}
