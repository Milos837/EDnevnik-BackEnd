package com.example.final_project_test.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.CourseEntity;
import com.example.final_project_test.entities.enums.ECourseSemester;
import com.example.final_project_test.entities.enums.ESchoolYear;

public interface CourseRepository extends CrudRepository<CourseEntity, Integer> {
	
	Boolean existsByNameAndSemesterAndYear(String name, ECourseSemester semester, ESchoolYear year);

}
