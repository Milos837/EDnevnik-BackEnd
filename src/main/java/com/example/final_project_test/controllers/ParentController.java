package com.example.final_project_test.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project_test.controllers.util.RESTError;
import com.example.final_project_test.entities.ParentEntity;
import com.example.final_project_test.repositories.ParentRepository;


@RestController
@RequestMapping(value = "/api/v1/parents")
public class ParentController {
	
	@Autowired
	private ParentRepository parentRepository;

	// Vrati sve
	@GetMapping(value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<ParentEntity>>((List<ParentEntity>) parentRepository.findAll(), HttpStatus.OK);
	}

	// Vrati po ID-u
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (parentRepository.existsById(id)) {
			return new ResponseEntity<ParentEntity>(parentRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
	}

}
