package com.example.final_project_test.services;

import java.util.List;

import com.example.final_project_test.entities.StudentEntity;

public interface ClassService {

	List<StudentEntity> addCoursesForEntireClass(Integer classId, Integer courseId, Integer teacherId);
	
	Boolean isActive(Integer classId);

}
