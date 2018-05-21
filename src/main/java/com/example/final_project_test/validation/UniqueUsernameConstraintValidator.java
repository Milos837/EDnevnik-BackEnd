package com.example.final_project_test.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.final_project_test.repositories.AdminRepository;
import com.example.final_project_test.repositories.ParentRepository;
import com.example.final_project_test.repositories.StudentRepository;
import com.example.final_project_test.repositories.TeacherRepository;

public class UniqueUsernameConstraintValidator implements ConstraintValidator<UniqueUsername, String>{
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private ParentRepository parentRepository;
	
	@Autowired
	private AdminRepository adminRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(teacherRepository.existsByUsername(value) || studentRepository.existsByUsername(value)
				|| parentRepository.existsByUsername(value) || adminRepository.existsByUsername(value)) {
			return false;
		}
		return true;
	}
	
	

}
