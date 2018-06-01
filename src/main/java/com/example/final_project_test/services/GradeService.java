package com.example.final_project_test.services;

import org.springframework.http.ResponseEntity;

import com.example.final_project_test.entities.StudentTeacherCourseEntity;
import com.example.final_project_test.entities.dto.GradeDto;

public interface GradeService {
	
	ResponseEntity<?> gradeStudent(GradeDto newGrade, Integer studentId, Integer teacherId, Integer courseId);
	
	Boolean checkForFinalGrade(StudentTeacherCourseEntity stce);

}
