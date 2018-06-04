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

import com.example.final_project_test.config.Encryption;
import com.example.final_project_test.controllers.util.RESTError;
import com.example.final_project_test.entities.ParentEntity;
import com.example.final_project_test.entities.dto.ParentDto;
import com.example.final_project_test.repositories.ParentRepository;
import com.example.final_project_test.repositories.RoleRepository;
import com.example.final_project_test.validation.ParentCustomValidator;

@RestController
@RequestMapping(value = "/api/v1/parents")
public class ParentController {

	@Autowired
	private ParentRepository parentRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ParentCustomValidator parentValidator;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(parentValidator);
	}
	
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

	// Dodaj novi
	@PostMapping(value = "/")
	public ResponseEntity<?> createNew(@Valid @RequestBody ParentDto newParent, BindingResult result) {
		if(result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			parentValidator.validate(newParent, result);
		}
		ParentEntity parent = new ParentEntity();
		parent.setFirstName(newParent.getFirstName());
		parent.setLastName(newParent.getLastName());
		parent.setUsername(newParent.getUsername());
		parent.setPassword(Encryption.getPassEncoded(newParent.getPassword()));
		parent.setEmail(newParent.getEmail());
		parent.setRole(roleRepository.findById(4).get());
		parentRepository.save(parent);
		return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
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
