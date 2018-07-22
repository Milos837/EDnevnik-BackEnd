package com.example.final_project_test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.final_project_test.entities.StudentTeacherCourseEntity;
import com.example.final_project_test.repositories.StudentTeacherCourseRepository;

@Service
public class StudentTeacherCourseServiceImpl implements StudentTeacherCourseService{
	
	@Autowired
	private StudentTeacherCourseRepository studentTeacherCourseRepository;
	
	@Override
	public Boolean isActive(Integer id) {
		if(studentTeacherCourseRepository.existsById(id)) {
			StudentTeacherCourseEntity stce = studentTeacherCourseRepository.findById(id).get();
			if(stce.getDeleted().equals(true) 
					|| stce.getTeacherCourse().getCourse().getDeleted().equals(true)
					|| stce.getTeacherCourse().getTeacher().getDeleted().equals(true)
					|| stce.getStudent().getDeleted().equals(true)) {
				return false;
			}
			return true;
		}
		return false;
	}

}
