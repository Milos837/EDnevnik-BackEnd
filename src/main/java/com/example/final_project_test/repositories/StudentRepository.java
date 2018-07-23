package com.example.final_project_test.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.ClassEntity;
import com.example.final_project_test.entities.ParentEntity;
import com.example.final_project_test.entities.StudentEntity;

public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {

	Boolean existsByUsername(String username);

	Boolean existsByAttendingClass(ClassEntity attendingClass);

	List<StudentEntity> findByAttendingClass(ClassEntity attendingClass);
	
	List<StudentEntity> findByParent(ParentEntity parent);
	
	StudentEntity findByUsername(String username);

}
