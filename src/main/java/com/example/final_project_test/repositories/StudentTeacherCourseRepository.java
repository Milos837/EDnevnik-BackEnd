package com.example.final_project_test.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.final_project_test.entities.StudentEntity;
import com.example.final_project_test.entities.StudentTeacherCourseEntity;
import com.example.final_project_test.entities.TeacherCourseEntity;

public interface StudentTeacherCourseRepository extends CrudRepository<StudentTeacherCourseEntity, Integer> {

	Boolean existsByStudentAndTeacherCourse(StudentEntity student, TeacherCourseEntity teacherCourse);
	
	StudentTeacherCourseEntity findByStudentAndTeacherCourse(StudentEntity student, TeacherCourseEntity teacherCourse);
	
	List<StudentTeacherCourseEntity> findByStudent(StudentEntity student);

}
