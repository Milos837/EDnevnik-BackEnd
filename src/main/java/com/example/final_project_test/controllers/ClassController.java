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
import com.example.final_project_test.entities.ClassEntity;
import com.example.final_project_test.repositories.ClassRepository;

@RestController
@RequestMapping(value = "/api/v1/classes")
public class ClassController {

	@Autowired
	private ClassRepository classRepository;

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
	public ResponseEntity<?> createNew(@RequestBody ClassEntity classEntity) {
		return new ResponseEntity<ClassEntity>(classRepository.save(classEntity), HttpStatus.OK);
	}

	// Izmeni po ID-u
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateById(@PathVariable Integer id, @RequestBody ClassEntity classEntity) {
		if (classRepository.existsById(id)) {
			ClassEntity c = classRepository.findById(id).get();
			if (classEntity.getName() != null && classEntity.getName() != " " && classEntity.getName() != "") {
				c.setName(classEntity.getName());
			}
			if (classEntity.getYear() != null) {
				c.setYear(classEntity.getYear());
			}
			return new ResponseEntity<ClassEntity>(classRepository.save(c), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(1, "Class not found."), HttpStatus.NOT_FOUND);
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

}
