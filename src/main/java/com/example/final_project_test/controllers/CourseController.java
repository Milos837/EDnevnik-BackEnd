package com.example.final_project_test.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project_test.controllers.util.RESTError;
import com.example.final_project_test.entities.CourseEntity;
import com.example.final_project_test.repositories.CourseRepository;

@RestController
@RequestMapping(value = "/api/v1/courses")
public class CourseController {

	@Autowired
	private CourseRepository courseRepository;

	// Vrati sve
	@GetMapping(value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<CourseEntity>>((List<CourseEntity>) courseRepository.findAll(), HttpStatus.OK);
	}

	// Vrati po ID-u
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (courseRepository.existsById(id)) {
			return new ResponseEntity<CourseEntity>(courseRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj novi
	@PostMapping(value = "/")
	public ResponseEntity<?> createNew(@RequestBody CourseEntity courseEntity) {
		return new ResponseEntity<CourseEntity>(courseRepository.save(courseEntity), HttpStatus.OK);
	}

	// Izmeni po ID-u
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateById(@PathVariable Integer id, @RequestBody CourseEntity courseEntity) {
		if (courseRepository.existsById(id)) {
			CourseEntity course = courseRepository.findById(id).get();
			if (courseEntity.getName() != null && courseEntity.getName() != " " && courseEntity.getName() != "") {
				course.setName(courseEntity.getName());
			}
			if (courseEntity.getWeeklyHours() != null) {
				course.setWeeklyHours(courseEntity.getWeeklyHours());
			}
			if (courseEntity.getYear() != null) {
				course.setYear(courseEntity.getYear());
			}
			if (courseEntity.getSemester() != null) {
				course.setSemester(courseEntity.getSemester());
			}
			return new ResponseEntity<CourseEntity>(courseRepository.save(course), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
	}

	// Obrisi po ID-u
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		if (courseRepository.existsById(id)) {
			CourseEntity temp = courseRepository.findById(id).get();
			courseRepository.deleteById(id);
			return new ResponseEntity<CourseEntity>(temp, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
	}

}
