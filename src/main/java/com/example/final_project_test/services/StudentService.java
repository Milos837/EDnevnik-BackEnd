package com.example.final_project_test.services;

import com.example.final_project_test.entities.StudentEntity;

public interface StudentService {
	
	StudentEntity addCourseForStudent(Integer studentId, Integer courseId, Integer teacherId);
	
	Boolean isActive(Integer studentId);

}
