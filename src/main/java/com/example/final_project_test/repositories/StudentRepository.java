package com.example.final_project_test.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.StudentEntity;

public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {
	
	Boolean existsByUsername(String username);

}
