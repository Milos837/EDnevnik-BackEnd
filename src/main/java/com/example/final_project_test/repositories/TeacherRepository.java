package com.example.final_project_test.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.TeacherEntity;

public interface TeacherRepository extends CrudRepository<TeacherEntity, Integer> {

}
