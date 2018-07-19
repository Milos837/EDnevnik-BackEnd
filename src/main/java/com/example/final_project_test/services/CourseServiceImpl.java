package com.example.final_project_test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.final_project_test.entities.CourseEntity;
import com.example.final_project_test.repositories.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService{
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Override
	public Boolean isActive(Integer courseId) {
		if(courseRepository.existsById(courseId)) {
			CourseEntity course = courseRepository.findById(courseId).get();
			if(course.getDeleted().equals(true)) {
				return false;
			}
			return true;
		}
		return false;
	}

}
