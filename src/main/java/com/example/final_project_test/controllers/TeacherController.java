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
import com.example.final_project_test.entities.CourseEntity;
import com.example.final_project_test.entities.TeacherCourseEntity;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.entities.dto.TeacherDto;
import com.example.final_project_test.entities.util.RESTError;
import com.example.final_project_test.repositories.AdminRepository;
import com.example.final_project_test.repositories.CourseRepository;
import com.example.final_project_test.repositories.RoleRepository;
import com.example.final_project_test.repositories.TeacherCourseRepository;
import com.example.final_project_test.repositories.TeacherRepository;
import com.example.final_project_test.services.CourseService;
import com.example.final_project_test.services.TeacherService;
import com.example.final_project_test.validation.TeacherCustomValidator;

@RestController
@RequestMapping(value = "/api/v1/teachers")
public class TeacherController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private TeacherCourseRepository teacherCourseRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private CourseService courseService;

	@Autowired
	private TeacherCustomValidator teacherValidator;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(teacherValidator);
	}

	// Vrati sve
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<TeacherEntity>>(
				((List<TeacherEntity>) teacherRepository.findAll())
				.stream().filter(teacher -> !teacher.getDeleted().equals(true))
				.collect(Collectors.toList()), HttpStatus.OK);
	}

	// Vrati po ID-u
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		if (!principal.getName().equals(teacherRepository.findById(id).get().getUsername()) 
				&& !adminRepository.existsByUsername(principal.getName())) {
			throw new AuthorizationServiceException("Forbidden");
		}
		if (teacherRepository.existsById(id) && teacherService.isActive(id)) {
			return new ResponseEntity<TeacherEntity>(teacherRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj novog profesora
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/")
	public ResponseEntity<?> createNew(@Valid @RequestBody TeacherDto newTeacher, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			teacherValidator.validate(newTeacher, result);
		}
		TeacherEntity teacher = new TeacherEntity();
		teacher.setDeleted(false);
		teacher.setFirstName(newTeacher.getFirstName());
		teacher.setLastName(newTeacher.getLastName());
		teacher.setUsername(newTeacher.getUsername());
		teacher.setPassword(Encryption.getPassEncoded(newTeacher.getPassword()));
		teacher.setRole(roleRepository.findById(2).get());
		teacherRepository.save(teacher);
		logger.info("Added teacher: " + newTeacher.toString());
		return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
	}
	
	//	Izmeni profesora
	@Secured("ROLE_ADMIN")
	@PutMapping(value = "/{teacherId}")
	public ResponseEntity<?> updateTeacher(@PathVariable Integer teacherId, @Valid @RequestBody TeacherDto uteacher, 
			BindingResult result) {
		if(teacherRepository.existsById(teacherId) && teacherService.isActive(teacherId)) {
			if (result.hasErrors()) {
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}
			TeacherEntity teacher = teacherRepository.findById(teacherId).get();
			teacher.setFirstName(uteacher.getFirstName());
			teacher.setLastName(uteacher.getLastName());
			teacherRepository.save(teacher);
			logger.info("Updated teacher with ID: " + teacherId.toString());
			return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
	}
	
	//	Obrisi profesora
	@Secured("ROLE_ADMIN")
	@DeleteMapping(value = "/{teacherId}")
	public ResponseEntity<?> deleteTeacher(@PathVariable Integer teacherId) {
		if(teacherRepository.existsById(teacherId) && teacherService.isActive(teacherId)) {
			TeacherEntity teacher = teacherRepository.findById(teacherId).get();
			teacher.setDeleted(true);
			teacherRepository.save(teacher);
			logger.info("Deleted teacher with ID: " + teacherId.toString());
			return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj predmet za profesora
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/{teacherId}/courses/{courseId}")
	public ResponseEntity<?> addCourseForTeacher(@PathVariable Integer teacherId, @PathVariable Integer courseId) {
		if (teacherRepository.existsById(teacherId) && teacherService.isActive(teacherId)) {
			if (courseRepository.existsById(courseId) && courseService.isActive(courseId)) {
				TeacherEntity teacher = teacherRepository.findById(teacherId).get();
				CourseEntity course = courseRepository.findById(courseId).get();
				if (!teacherCourseRepository.existsByTeacherAndCourse(teacher, course)) {
					TeacherCourseEntity TCE = new TeacherCourseEntity();
					TCE.setDeleted(false);
					TCE.setTeacher(teacherRepository.findById(teacherId).get());
					TCE.setCourse(courseRepository.findById(courseId).get());
					teacherCourseRepository.save(TCE);
					return new ResponseEntity<TeacherEntity>(teacherRepository.findById(teacherId).get(),
							HttpStatus.OK);
				} else if (teacherCourseRepository.existsByTeacherAndCourse(teacher, course)
							&& teacherCourseRepository.findByTeacherAndCourse(teacher, course).getDeleted().equals(true)) {
					TeacherCourseEntity TCE = teacherCourseRepository.findByTeacherAndCourse(teacher, course);
					TCE.setDeleted(false);
					teacherCourseRepository.save(TCE);
					return new ResponseEntity<TeacherEntity>(teacherRepository.findById(teacherId).get(),
							HttpStatus.OK);
				}
				return new ResponseEntity<RESTError>(new RESTError(11, "Teacher course combination not found."),
						HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
	}

	// Obrisi predmet za profesora
	@Secured("ROLE_ADMIN")
	@DeleteMapping(value = "/{teacherId}/courses/{courseId}")
	public ResponseEntity<?> deleteCourseForTeacher(@PathVariable Integer teacherId, @PathVariable Integer courseId) {
		if (teacherRepository.existsById(teacherId) && teacherService.isActive(teacherId)) {
			if (courseRepository.existsById(courseId) && courseService.isActive(courseId)) {
				TeacherEntity teacher = teacherRepository.findById(teacherId).get();
				CourseEntity course = courseRepository.findById(courseId).get();
				if (teacherCourseRepository.existsByTeacherAndCourse(teacher, course)) {
					TeacherCourseEntity teacherCourse = teacherCourseRepository.findByTeacherAndCourse(teacher, course);
					teacherCourse.setDeleted(true);
					teacherCourseRepository.save(teacherCourse);
					return new ResponseEntity<TeacherEntity>(teacherRepository.findById(teacherId).get(),
							HttpStatus.OK);
				}
				return new ResponseEntity<RESTError>(new RESTError(7, "Teacher doesn't teach this course."),
						HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
	}
	
	//	Prikazi sve predmete profesora
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/{teacherId}/courses/")
	public ResponseEntity<?> getCoursesForTeacher(@PathVariable Integer teacherId) {
		if (teacherRepository.existsById(teacherId) && teacherService.isActive(teacherId)) {
			TeacherEntity teacher = teacherRepository.findById(teacherId).get();
			
			List<CourseEntity> courses = ((List<TeacherCourseEntity>) teacherCourseRepository.findByTeacher(teacher))
				.stream()
					.filter(teacherCourse -> !teacherCourse.getDeleted().equals(true))
					.map(course -> course.getCourse())
					.filter(course -> !course.getDeleted().equals(true))
					.collect(Collectors.toList());
			
			return new ResponseEntity<List<CourseEntity>>(courses, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
	}

	public String createErrorMessage(BindingResult result) {
		// return
		// result.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining(","));
		String errors = "";
		for (ObjectError error : result.getAllErrors()) {
			errors += error.getDefaultMessage();
			errors += "\n";
		}
		return errors;
	}

}
