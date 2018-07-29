package com.example.final_project_test.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.example.final_project_test.entities.CourseEntity;
import com.example.final_project_test.entities.TeacherCourseEntity;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.entities.dto.CourseDto;
import com.example.final_project_test.entities.util.RESTError;
import com.example.final_project_test.repositories.CourseRepository;
import com.example.final_project_test.repositories.TeacherCourseRepository;
import com.example.final_project_test.services.CourseService;
import com.example.final_project_test.validation.CourseCustomValidator;

@RestController
@RequestMapping(value = "/api/v1/courses")
public class CourseController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private TeacherCourseRepository teacherCourseRepository;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseCustomValidator courseValidator;
	
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(courseValidator);
	}

	// Vrati sve
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<CourseEntity>>(((List<CourseEntity>) courseRepository.findAll())
				.stream().filter(course -> !course.getDeleted().equals(true))
				.collect(Collectors.toList()), HttpStatus.OK);
	}

	// Vrati po ID-u
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (courseRepository.existsById(id) && courseService.isActive(id)) {
			return new ResponseEntity<CourseEntity>(courseRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj novi
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/")
	public ResponseEntity<?> createNew(@Valid @RequestBody CourseDto newCourse, BindingResult result) {
		if(result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			courseValidator.validate(newCourse, result);
		}
		CourseEntity course = new CourseEntity();
		course.setDeleted(false);
		course.setName(newCourse.getName());
		course.setWeeklyHours(newCourse.getWeeklyHours());
		course.setSemester(newCourse.getSemester());
		course.setYear(newCourse.getYear());
		courseRepository.save(course);
		logger.info("Added course: " + newCourse.toString());
		return new ResponseEntity<CourseEntity>(course, HttpStatus.OK);
	}
	
	//	izmeni predmet
	@Secured("ROLE_ADMIN")
	@PutMapping(value = "/{courseId}")
	public ResponseEntity<?> updateCourse(@PathVariable Integer courseId, @Valid @RequestBody CourseDto ucourse, 
			BindingResult result) {
		if (courseRepository.existsById(courseId) && courseService.isActive(courseId)) {
			if(result.hasErrors()) {
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}
			CourseEntity course = courseRepository.findById(courseId).get();
			course.setYear(ucourse.getYear());
			course.setSemester(ucourse.getSemester());
			course.setWeeklyHours(ucourse.getWeeklyHours());
			courseRepository.save(course);
			logger.info("Updated course with ID: " + courseId.toString());
			return new ResponseEntity<CourseEntity>(course, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
	}

	

	// Obrisi po ID-u
	@Secured("ROLE_ADMIN")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		if (courseRepository.existsById(id) && courseService.isActive(id)) {
			CourseEntity temp = courseRepository.findById(id).get();
			temp.setDeleted(true);
			courseRepository.save(temp);
			logger.info("Deleted course with ID: " + id.toString());
			return new ResponseEntity<CourseEntity>(temp, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
	}
	
	//	Vrati sve profesore koji preadaju zadati kurs
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/{courseId}/teachers/")
	public ResponseEntity<?> getTeachersForCourse(@PathVariable Integer courseId) {
		if (courseRepository.existsById(courseId) && courseService.isActive(courseId)) {
			CourseEntity course = courseRepository.findById(courseId).get();
			
			List<TeacherEntity> teachers = ((List<TeacherCourseEntity>) teacherCourseRepository.findByCourse(course))
					.stream().map(teacher -> teacher.getTeacher())
					.filter(teacher -> !teacher.getDeleted().equals(true))
					.collect(Collectors.toList());
			
			return new ResponseEntity<List<TeacherEntity>>(teachers, HttpStatus.OK);
		}
		return null;
	}
	
	//	Vrati sve profesor-predmet kombinacije
	@Secured("ROLE_ADMIN")
	@GetMapping(value ="/teacher-course/")
	public ResponseEntity<?> getAllTeacherCourseCombinations() {
		List<TeacherCourseEntity> teacherCourses = ((List<TeacherCourseEntity>) teacherCourseRepository.findAll())
				.stream()
					.filter(teacherCourse -> (!teacherCourse.getDeleted().equals(true) 
							&& !teacherCourse.getCourse().getDeleted().equals(true)
							&& !teacherCourse.getTeacher().getDeleted().equals(true)))
					.collect(Collectors.toList());
		
		return new ResponseEntity<List<TeacherCourseEntity>>(teacherCourses, HttpStatus.OK);
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
