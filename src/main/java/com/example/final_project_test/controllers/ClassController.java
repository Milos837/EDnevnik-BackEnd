package com.example.final_project_test.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project_test.controllers.util.RESTError;
import com.example.final_project_test.entities.ClassEntity;
import com.example.final_project_test.entities.dto.ClassDto;
import com.example.final_project_test.repositories.ClassRepository;
import com.example.final_project_test.validation.ClassCustomValidator;

@RestController
@RequestMapping(value = "/api/v1/classes")
public class ClassController {

	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private ClassCustomValidator classValidator;
	
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(classValidator);
	}

	// Vrati sve
	@GetMapping(value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<ClassEntity>>((List<ClassEntity>) classRepository.findAll(), HttpStatus.OK);
	}

	// Vrati po ID-u
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (classRepository.existsById(id)) {
			return new ResponseEntity<ClassEntity>(classRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(1, "Class not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj novi
	@PostMapping(value = "/")
	public ResponseEntity<?> createNew(@Valid @RequestBody ClassDto newClass, BindingResult result) {
		if(result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			classValidator.validate(newClass, result);
		}
		ClassEntity classEntity = new ClassEntity();
		classEntity.setClassNumber(newClass.getClassNumber());
		classEntity.setYear(newClass.getYear());
		classRepository.save(classEntity);
		return new ResponseEntity<ClassEntity>(classEntity, HttpStatus.OK);
	}


	// Obrisi po ID-u
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		if (classRepository.existsById(id)) {
			ClassEntity temp = classRepository.findById(id).get();
			classRepository.deleteById(id);
			return new ResponseEntity<ClassEntity>(temp, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(1, "Class not found."), HttpStatus.NOT_FOUND);
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
