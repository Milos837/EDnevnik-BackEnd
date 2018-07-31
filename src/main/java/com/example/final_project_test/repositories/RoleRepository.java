package com.example.final_project_test.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
	
	RoleEntity findByName(String name);

}
