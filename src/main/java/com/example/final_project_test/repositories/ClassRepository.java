package com.example.final_project_test.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.ClassEntity;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.entities.enums.ESchoolYear;

public interface ClassRepository extends CrudRepository<ClassEntity, Integer> {
	
	Boolean existsByClassNumberAndYear(String classNumber, ESchoolYear year);
	
	Boolean existsBySupervisorTeacher(TeacherEntity teacher);

}
