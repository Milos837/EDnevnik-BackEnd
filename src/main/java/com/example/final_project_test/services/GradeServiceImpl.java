package com.example.final_project_test.services;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.final_project_test.controllers.util.RESTError;
import com.example.final_project_test.entities.CourseEntity;
import com.example.final_project_test.entities.GradeEntity;
import com.example.final_project_test.entities.StudentEntity;
import com.example.final_project_test.entities.StudentTeacherCourseEntity;
import com.example.final_project_test.entities.TeacherCourseEntity;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.entities.dto.GradeDto;
import com.example.final_project_test.repositories.CourseRepository;
import com.example.final_project_test.repositories.GradeRepository;
import com.example.final_project_test.repositories.StudentRepository;
import com.example.final_project_test.repositories.StudentTeacherCourseRepository;
import com.example.final_project_test.repositories.TeacherCourseRepository;
import com.example.final_project_test.repositories.TeacherRepository;

@Service
public class GradeServiceImpl implements GradeService{
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private TeacherCourseRepository teacherCourseRepository;
	
	@Autowired
	private StudentTeacherCourseRepository studentTeacherCourseRepository;
	
	@Autowired
	private GradeRepository gradeRepository;
	
	public ResponseEntity<?> gradeStudent(GradeDto newGrade, Integer studentId, Integer teacherId, Integer courseId) {
		StudentEntity student = studentRepository.findById(studentId).get();
		TeacherEntity teacher = teacherRepository.findById(teacherId).get();
		CourseEntity course = courseRepository.findById(courseId).get();
		if(teacherCourseRepository.existsByTeacherAndCourse(teacher, course)) {
			TeacherCourseEntity teacherCourse = teacherCourseRepository.findByTeacherAndCourse(teacher, course);
			if(studentTeacherCourseRepository.existsByStudentAndTeacherCourse(student, teacherCourse)) {
				StudentTeacherCourseEntity stce = studentTeacherCourseRepository.findByStudentAndTeacherCourse(student, teacherCourse);
				if(!checkForFinalGrade(stce)) {
					GradeEntity grade = new GradeEntity();
					grade.setStudentTeacherCourse(stce);
					grade.setValue(newGrade.getValue());
					grade.setType(newGrade.getType());
					grade.setDateUTC(ZonedDateTime.now(ZoneOffset.UTC));
					grade.setFinalGrade(false);
					gradeRepository.save(grade);
					return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
				}
				return new ResponseEntity<RESTError>(new RESTError(13, "Student already has final grade."), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError(12, "Student teacher course combination not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(7, "Teacher doesn't teach this course."), HttpStatus.NOT_FOUND);
	}
	
	//	Proveri da li student ima zakljucnu ocenu iz predmeta kod nastavnika
	public Boolean checkForFinalGrade(StudentTeacherCourseEntity stce) {
		List<GradeEntity> grades = gradeRepository.findByStudentTeacherCourse(stce);
		for (GradeEntity gradeEntity : grades) {
			if(gradeEntity.getFinalGrade()) {
				return true;
			}
		}
		return false;
	}

}
