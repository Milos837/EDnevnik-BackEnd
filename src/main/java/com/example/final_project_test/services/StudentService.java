package com.example.final_project_test.services;

import java.util.List;

import com.example.final_project_test.entities.StudentEntity;
import com.example.final_project_test.entities.TeacherCourseEntity;

public interface StudentService {
	
	StudentEntity addCourseForStudent(Integer studentId, Integer courseId, Integer teacherId);
	
	Boolean isActive(Integer studentId);
	
	List<TeacherCourseEntity> getCourses(Integer studentId);

}
