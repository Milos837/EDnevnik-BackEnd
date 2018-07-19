package com.example.final_project_test.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project_test.config.Encryption;
import com.example.final_project_test.controllers.util.RESTError;
import com.example.final_project_test.entities.CourseEntity;
import com.example.final_project_test.entities.StudentEntity;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.entities.dto.StudentDto;
import com.example.final_project_test.repositories.ClassRepository;
import com.example.final_project_test.repositories.CourseRepository;
import com.example.final_project_test.repositories.ParentRepository;
import com.example.final_project_test.repositories.RoleRepository;
import com.example.final_project_test.repositories.StudentRepository;
import com.example.final_project_test.repositories.TeacherCourseRepository;
import com.example.final_project_test.repositories.TeacherRepository;
import com.example.final_project_test.services.StudentService;
import com.example.final_project_test.validation.StudentCustomValidator;

@RestController
@RequestMapping(value = "/api/v1/students")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private StudentService studentService;

	@Autowired
	private TeacherCourseRepository teacherCourseRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private StudentCustomValidator studentValidator;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(studentValidator);
	}

	// Vrati sve
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<List<StudentEntity>>((List<StudentEntity>) studentRepository.findAll(),
				HttpStatus.OK);
	}

	// Vrati po ID-u
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		if (studentRepository.existsById(id)) {
			return new ResponseEntity<StudentEntity>(studentRepository.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj novi
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/")
	public ResponseEntity<?> createNew(@Valid @RequestBody StudentDto newStudent, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			studentValidator.validate(newStudent, result);
		}
		StudentEntity student = new StudentEntity();
		student.setDeleted(false);
		student.setFirstName(newStudent.getFirstName());
		student.setLastName(newStudent.getLastName());
		student.setUsername(newStudent.getUsername());
		student.setPassword(Encryption.getPassEncoded(newStudent.getPassword()));
		student.setRole(roleRepository.findById(3).get());
		studentRepository.save(student);
		return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
	}

	// Dodaj odeljenje za ucenika
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/{studentId}/class/{classId}")
	public ResponseEntity<?> addClass(@PathVariable Integer studentId, @PathVariable Integer classId) {
		if (studentRepository.existsById(studentId)) {
			if (classRepository.existsById(classId)) {
				StudentEntity student = studentRepository.findById(studentId).get();
				student.setAttendingClass(classRepository.findById(classId).get());
				studentRepository.save(student);
				return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Class not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
	}

	// Promeni odeljenje za ucenika
	@Secured("ROLE_ADMIN")
	@PutMapping(value = "/{studentId}/class/{classId}")
	public ResponseEntity<?> updateClass(@PathVariable Integer studentId, @PathVariable Integer classId) {
		if (studentRepository.existsById(studentId)) {
			if (classRepository.existsById(classId)) {
				StudentEntity student = studentRepository.findById(studentId).get();
				student.setAttendingClass(classRepository.findById(classId).get());
				studentRepository.save(student);
				return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Class not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj roditelja uceniku
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/{studentId}/parent/{parentId}")
	public ResponseEntity<?> addParent(@PathVariable Integer studentId, @PathVariable Integer parentId) {
		if (studentRepository.existsById(studentId)) {
			if (parentRepository.existsById(parentId)) {
				StudentEntity student = studentRepository.findById(studentId).get();
				student.setParent(parentRepository.findById(parentId).get());
				studentRepository.save(student);
				return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj predmet studentu
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/{studentId}/courses/{courseId}/teachers/{teacherId}")
	public ResponseEntity<?> addCourseForStudent(@PathVariable Integer studentId, @PathVariable Integer courseId,
			@PathVariable Integer teacherId) {
		if (studentRepository.existsById(studentId)) {
			if (courseRepository.existsById(courseId)) {
				if (teacherRepository.existsById(teacherId)) {
					TeacherEntity teacher = teacherRepository.findById(teacherId).get();
					CourseEntity course = courseRepository.findById(courseId).get();
					if (teacherCourseRepository.existsByTeacherAndCourse(teacher, course)) {
						StudentEntity student = studentService.addCourseForStudent(studentId, courseId, teacherId);
						if (student != null) {
							return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
						}
						return new ResponseEntity<RESTError>(new RESTError(10, "Student already has that course."),
								HttpStatus.NOT_FOUND);
					}
					return new ResponseEntity<RESTError>(new RESTError(11, "Teacher course combination not found."),
							HttpStatus.NOT_FOUND);
				}
				return new ResponseEntity<RESTError>(new RESTError(6, "Teacher not found."), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError(2, "Course not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
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
