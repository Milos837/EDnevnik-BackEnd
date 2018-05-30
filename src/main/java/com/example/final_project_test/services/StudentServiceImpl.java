package com.example.final_project_test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.final_project_test.entities.CourseEntity;
import com.example.final_project_test.entities.StudentEntity;
import com.example.final_project_test.entities.StudentTeacherCourseEntity;
import com.example.final_project_test.entities.TeacherCourseEntity;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.repositories.CourseRepository;
import com.example.final_project_test.repositories.StudentRepository;
import com.example.final_project_test.repositories.StudentTeacherCourseRepository;
import com.example.final_project_test.repositories.TeacherCourseRepository;
import com.example.final_project_test.repositories.TeacherRepository;

@Service
public class StudentServiceImpl implements StudentService{
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private TeacherCourseRepository teacherCourseRepository;
	
	@Autowired
	private StudentTeacherCourseRepository studentTeacherCourseRepository;

	@Override
	public StudentEntity addCourseForStudent(Integer studentId, Integer courseId, Integer teacherId) {
		StudentEntity student = studentRepository.findById(studentId).get();
		CourseEntity course = courseRepository.findById(courseId).get();
		TeacherEntity teacher = teacherRepository.findById(teacherId).get();
		TeacherCourseEntity teacherCourse = teacherCourseRepository.findByTeacherAndCourse(teacher, course);
		
		if(!studentTeacherCourseRepository.existsByStudentAndTeacherCourse(student, teacherCourse)) {
			StudentTeacherCourseEntity stce = new StudentTeacherCourseEntity();
			stce.setStudent(student);
			stce.setTeacherCourse(teacherCourse);
			studentTeacherCourseRepository.save(stce);
			
			return student;
		}
		return null;
	}
	
	

}
