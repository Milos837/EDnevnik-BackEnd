package com.example.final_project_test.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.CourseEntity;

public interface CourseRepository extends CrudRepository<CourseEntity, Integer> {

}
