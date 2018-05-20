package com.example.final_project_test.controllers;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project_test.controllers.util.RESTError;
import com.example.final_project_test.controllers.util.TeacherCustomValidator;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.entities.dto.TeacherDto;
import com.example.final_project_test.repositories.TeacherRepository;

@RestController
@RequestMapping(value = "/api/v1/teachers")
public class TeacherController {

	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private TeacherCustomValidator teacherValidator;
	
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(teacherValidator);
	}

	// Vrati sve
	@GetMapping(value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<TeacherEntity>>((List<TeacherEntity>) teacherRepository.findAll(),
				HttpStatus.OK);
	}

	// Vrati po ID-u
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (teacherRepository.existsById(id)) {
			return new ResponseEntity<TeacherEntity>(teacherRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj novi
	@PostMapping(value = "/")
	public ResponseEntity<?> createNew(@Valid @RequestBody TeacherDto newTeacher, BindingResult result) {
		if(result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			teacherValidator.validate(newTeacher, result);
		}
		TeacherEntity teacher = new TeacherEntity();
		teacher.setFirstName(newTeacher.getFirstName());
		teacher.setLastName(newTeacher.getLastName());
		teacher.setUsername(newTeacher.getUsername());
		teacher.setPassword(newTeacher.getPassword());
		teacherRepository.save(teacher);
		return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
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
