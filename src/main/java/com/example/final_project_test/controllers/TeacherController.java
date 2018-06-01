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
import com.example.final_project_test.entities.CourseEntity;
import com.example.final_project_test.entities.TeacherCourseEntity;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.entities.dto.GradeDto;
import com.example.final_project_test.entities.dto.TeacherDto;
import com.example.final_project_test.repositories.CourseRepository;
import com.example.final_project_test.repositories.StudentRepository;
import com.example.final_project_test.repositories.TeacherCourseRepository;
import com.example.final_project_test.repositories.TeacherRepository;
import com.example.final_project_test.services.GradeService;
import com.example.final_project_test.validation.TeacherCustomValidator;

@RestController
@RequestMapping(value = "/api/v1/teachers")
public class TeacherController {

	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private TeacherCourseRepository	teacherCourseRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private GradeService gradeService;
	
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
	
	//	Dodaj predmet za profesora
	@PostMapping(value = "/{teacherId}/courses/{courseId}")
	public ResponseEntity<?> addCourseForTeacher(@PathVariable Integer teacherId, @PathVariable Integer courseId) {
		if(teacherRepository.existsById(teacherId)) {
			if(courseRepository.existsById(courseId)) {
				TeacherEntity teacher = teacherRepository.findById(teacherId).get();
				CourseEntity course = courseRepository.findById(courseId).get();
				if(!teacherCourseRepository.existsByTeacherAndCourse(teacher, course)) {
					TeacherCourseEntity TCE = new TeacherCourseEntity();
					TCE.setTeacher(teacherRepository.findById(teacherId).get());
					TCE.setCourse(courseRepository.findById(courseId).get());
					teacherCourseRepository.save(TCE);
					return new ResponseEntity<TeacherEntity>(teacherRepository.findById(teacherId).get(), HttpStatus.OK);
				}
				return new ResponseEntity<RESTError>(new RESTError(11, "Teacher course combination not found."), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
	}
	
	//	Obrisi predmet za profesora
	@DeleteMapping(value = "/{teacherId}/courses/{courseId}")
	public ResponseEntity<?> deleteCourseForTeacher(@PathVariable Integer teacherId, @PathVariable Integer courseId) {
		if(teacherRepository.existsById(teacherId)) {
			if(courseRepository.existsById(courseId)) {
				TeacherEntity teacher = teacherRepository.findById(teacherId).get();
				CourseEntity course = courseRepository.findById(courseId).get();
				if(teacherCourseRepository.existsByTeacherAndCourse(teacher, course)) {
					teacherCourseRepository.deleteById(teacherCourseRepository.findByTeacherAndCourse(teacher, course).getId());
					return new ResponseEntity<TeacherEntity>(teacherRepository.findById(teacherId).get(), HttpStatus.OK);
				}
				return new ResponseEntity<RESTError>(new RESTError(7, "Teacher doesn't teach this course."), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
	}
	
	//	Oceni ucenika
	@PostMapping(value = "/{teacherId}/courses/{courseId}/students/{studentId}/grade/")
	public ResponseEntity<?> gradeStudent(@PathVariable Integer teacherId, @PathVariable Integer courseId
			, @PathVariable Integer studentId, @Valid @RequestBody GradeDto newGrade, BindingResult result) {
		if(teacherRepository.existsById(teacherId)) {
			if(courseRepository.existsById(courseId)) {
				if (studentRepository.existsById(studentId)) {
					if(!result.hasErrors()) {
						return gradeService.gradeStudent(newGrade, studentId, teacherId, courseId);
					}
					return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
				}
				return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
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
