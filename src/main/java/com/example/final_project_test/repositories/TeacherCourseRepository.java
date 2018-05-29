package com.example.final_project_test.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.CourseEntity;
import com.example.final_project_test.entities.TeacherCourseEntity;
import com.example.final_project_test.entities.TeacherEntity;

public interface TeacherCourseRepository extends CrudRepository<TeacherCourseEntity, Integer> {
	
	Boolean existsByTeacherAndCourse(TeacherEntity teacher, CourseEntity course);
	
	TeacherCourseEntity findByTeacherAndCourse(TeacherEntity teacher, CourseEntity course);

}
