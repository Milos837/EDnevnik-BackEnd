package com.example.final_project_test.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project_test.controllers.util.RESTError;
import com.example.final_project_test.entities.GradeEntity;
import com.example.final_project_test.entities.dto.GradeDto;
import com.example.final_project_test.repositories.GradeRepository;
import com.example.final_project_test.repositories.StudentTeacherCourseRepository;
import com.example.final_project_test.services.GradeService;

@RestController
@RequestMapping(value = "/api/v1/grades")
public class GradeController {

	@Autowired
	private GradeRepository gradeRepository;
	
	@Autowired
	private StudentTeacherCourseRepository studentTeacherCourseRepository;
	
	@Autowired
	private GradeService gradeService;

	// Vrati sve
	@GetMapping(value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<GradeEntity>>((List<GradeEntity>) gradeRepository.findAll(), HttpStatus.OK);
	}

	// Vrati po ID-u
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (gradeRepository.existsById(id)) {
			return new ResponseEntity<GradeEntity>(gradeRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(3, "Grade not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj novi
	@PostMapping(value = "/{studentTeacherCourse}")
	public ResponseEntity<?> createNew(@PathVariable Integer studentTeacherCourse, @Valid @RequestBody GradeDto newGrade
			,BindingResult result) {
		if(studentTeacherCourseRepository.existsById(studentTeacherCourse)) {
			if(!result.hasErrors()) {
				return gradeService.gradeStudent(newGrade, studentTeacherCourse);
			}
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<RESTError>(new RESTError(12, "Student teacher course combination not found."), HttpStatus.NOT_FOUND);
	}


	// Obrisi po ID-u
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteByID(@PathVariable Integer id) {
		if (gradeRepository.existsById(id)) {
			GradeEntity temp = gradeRepository.findById(id).get();
			gradeRepository.deleteById(id);
			return new ResponseEntity<GradeEntity>(temp, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(3, "Grade not found."), HttpStatus.NOT_FOUND);
	}
	
	public String createErrorMessage(BindingResult result) {
		//return result.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining(","));
		String errors = "";
		for (ObjectError error : result.getAllErrors()) {
			errors += error.getDefaultMessage();
			errors += "\n";
		}
		return errors;
	}

}
