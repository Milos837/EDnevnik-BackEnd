package com.example.final_project_test.controllers;

import java.time.Instant;
import java.util.Date;
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
import com.example.final_project_test.entities.GradeEntity;
import com.example.final_project_test.repositories.GradeRepository;

@RestController
@RequestMapping(value = "/api/v1/grades")
public class GradeController {

	@Autowired
	private GradeRepository gradeRepository;

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
	@PostMapping(value = "/")
	public ResponseEntity<?> createNew(@RequestBody GradeEntity gradeEntity) {
		gradeEntity.setDate(Date.from(Instant.now()));
		gradeEntity.setFinalGrade(false);
		return new ResponseEntity<GradeEntity>(gradeRepository.save(gradeEntity), HttpStatus.OK);
	}

	// Izmeni po ID-u
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateById(@PathVariable Integer id, @RequestBody GradeEntity gradeEntity) {
		if (gradeRepository.existsById(id)) {
			GradeEntity grade = gradeRepository.findById(id).get();
			if (gradeEntity.getValue() != null) {
				grade.setValue(gradeEntity.getValue());
			}
			return new ResponseEntity<GradeEntity>(gradeRepository.save(grade), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(3, "Grade not found."), HttpStatus.NOT_FOUND);
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

}
