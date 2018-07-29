package com.example.final_project_test.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project_test.config.Encryption;
import com.example.final_project_test.entities.ParentEntity;
import com.example.final_project_test.entities.StudentEntity;
import com.example.final_project_test.entities.dto.ParentDto;
import com.example.final_project_test.entities.util.RESTError;
import com.example.final_project_test.repositories.AdminRepository;
import com.example.final_project_test.repositories.ParentRepository;
import com.example.final_project_test.repositories.RoleRepository;
import com.example.final_project_test.repositories.StudentRepository;
import com.example.final_project_test.services.ParentService;
import com.example.final_project_test.validation.ParentCustomValidator;

@RestController
@RequestMapping(value = "/api/v1/parents")
public class ParentController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private ParentRepository parentRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ParentService parentService;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private ParentCustomValidator parentValidator;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(parentValidator);
	}
	
	// Vrati sve
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<ParentEntity>>(((List<ParentEntity>) parentRepository.findAll())
				.stream().filter(parent -> !parent.getDeleted().equals(true))
				.collect(Collectors.toList()), HttpStatus.OK);
	}

	// Vrati po ID-u
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (parentRepository.existsById(id) && parentService.isActive(id)) {
			return new ResponseEntity<ParentEntity>(parentRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj novi
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/")
	public ResponseEntity<?> createNew(@Valid @RequestBody ParentDto newParent, BindingResult result) {
		if(result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			parentValidator.validate(newParent, result);
		}
		ParentEntity parent = new ParentEntity();
		parent.setDeleted(false);
		parent.setFirstName(newParent.getFirstName());
		parent.setLastName(newParent.getLastName());
		parent.setUsername(newParent.getUsername());
		parent.setPassword(Encryption.getPassEncoded(newParent.getPassword()));
		parent.setEmail(newParent.getEmail());
		parent.setRole(roleRepository.findById(4).get());
		parentRepository.save(parent);
		logger.info("Added parent: " + newParent.toString());
		return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
	}
	
	//	Izmeni roditelja
	@Secured("ROLE_ADMIN")
	@PutMapping(value = "/{parentId}")
	public ResponseEntity<?> updateParent(@PathVariable Integer parentId, @Valid @RequestBody ParentDto uparent, 
			BindingResult result) {
		if(parentRepository.existsById(parentId) && parentService.isActive(parentId)) {
			if (result.hasErrors()) {
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}
			ParentEntity parent = parentRepository.findById(parentId).get();
			parent.setFirstName(uparent.getFirstName());
			parent.setLastName(uparent.getLastName());
			parent.setEmail(uparent.getEmail());
			parentRepository.save(parent);
			logger.info("Updated parent with ID: " + parentId.toString());
			return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
	}
	
	//	Obrisi roditelja
	@Secured("ROLE_ADMIN")
	@DeleteMapping(value = "/{parentId}")
	public ResponseEntity<?> deleteParent(@PathVariable Integer parentId) {
		if(parentRepository.existsById(parentId) && parentService.isActive(parentId)) {
			ParentEntity parent = parentRepository.findById(parentId).get();
			parent.setDeleted(true);
			parentRepository.save(parent);
			logger.info("Deleted parent with ID: " + parentId.toString());
			return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
	}
	
	//	vrati svu decu roditelja
	@Secured({"ROLE_ADMIN", "ROLE_PARENT"})
	@GetMapping(value = "/{parentId}/children/")
	public ResponseEntity<?> getAllChildren(@PathVariable Integer parentId, HttpServletRequest request) {
		if(parentRepository.existsById(parentId) && parentService.isActive(parentId)) {
			Principal principal = request.getUserPrincipal();
			if (!principal.getName().equals(parentRepository.findById(parentId).get().getUsername())
					&& !adminRepository.existsByUsername(principal.getName())) {
				throw new AuthorizationServiceException("Forbidden");
			}
			ParentEntity parent = parentRepository.findById(parentId).get();
			List<StudentEntity> children = ((List<StudentEntity>) studentRepository.findByParent(parent))
					.stream()
						.filter(student -> !student.getDeleted().equals(true))
						.collect(Collectors.toList());
			return new ResponseEntity<List<StudentEntity>>(children, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
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
