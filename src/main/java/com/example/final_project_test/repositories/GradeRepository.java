package com.example.final_project_test.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.GradeEntity;
import com.example.final_project_test.entities.StudentTeacherCourseEntity;

public interface GradeRepository extends CrudRepository<GradeEntity, Integer> {
	
	List<GradeEntity> findByStudentTeacherCourse(StudentTeacherCourseEntity stce);

}
