package com.example.final_project_test.services;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;

import com.example.final_project_test.entities.GradeEntity;
import com.example.final_project_test.entities.StudentTeacherCourseEntity;
import com.example.final_project_test.entities.dto.GradeDto;

public interface GradeService {
	
	ResponseEntity<?> gradeStudent(GradeDto newGrade, Integer studentId, Integer teacherId, Integer courseId) throws MessagingException;
	
	ResponseEntity<?> gradeStudent(GradeDto newGrade, Integer studentTeacherCourse) throws MessagingException;
	
	Boolean checkForFinalGrade(StudentTeacherCourseEntity stce);
	
	void sendEmailToParent(StudentTeacherCourseEntity stc, GradeEntity grade) throws MessagingException;

}
