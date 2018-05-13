package com.example.final_project_test.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.GradeEntity;

public interface GradeRepository extends CrudRepository<GradeEntity, Integer> {

}
