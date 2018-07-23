package com.example.final_project_test.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.ParentEntity;

public interface ParentRepository extends CrudRepository<ParentEntity, Integer> {
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);
	
	ParentEntity findByUsername(String username);

}
