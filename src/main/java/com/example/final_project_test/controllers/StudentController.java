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
import com.example.final_project_test.entities.StudentEntity;
import com.example.final_project_test.entities.StudentTeacherCourseEntity;
import com.example.final_project_test.entities.TeacherCourseEntity;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.entities.dto.StudentDto;
import com.example.final_project_test.entities.util.RESTError;
import com.example.final_project_test.repositories.AdminRepository;
import com.example.final_project_test.repositories.ClassRepository;
import com.example.final_project_test.repositories.CourseRepository;
import com.example.final_project_test.repositories.ParentRepository;
import com.example.final_project_test.repositories.RoleRepository;
import com.example.final_project_test.repositories.StudentRepository;
import com.example.final_project_test.repositories.StudentTeacherCourseRepository;
import com.example.final_project_test.repositories.TeacherCourseRepository;
import com.example.final_project_test.repositories.TeacherRepository;
import com.example.final_project_test.services.ClassService;
import com.example.final_project_test.services.CourseService;
import com.example.final_project_test.services.ParentService;
import com.example.final_project_test.services.StudentService;
import com.example.final_project_test.services.StudentTeacherCourseService;
import com.example.final_project_test.services.TeacherCourseService;
import com.example.final_project_test.services.TeacherService;
import com.example.final_project_test.validation.StudentCustomValidator;

@RestController
@RequestMapping(value = "/api/v1/students")
public class StudentController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AdminRepository adminRepository;

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
	private ClassService classService;
	
	@Autowired
	private ParentService parentService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private TeacherService teacherService;

	@Autowired
	private TeacherCourseRepository teacherCourseRepository;
	
	@Autowired
	private StudentTeacherCourseRepository studentTeacherCourseRepository;
	
	@Autowired
	private StudentTeacherCourseService studentTeacherCourseService;

	@Autowired
	private TeacherCourseService teacherCourseService;
	
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
		return new ResponseEntity<List<StudentEntity>>(((List<StudentEntity>) studentRepository.findAll())
				.stream().filter(student -> !student.getDeleted().equals(true))
				.collect(Collectors.toList()), HttpStatus.OK);
	}

	// Vrati po ID-u
	@Secured({"ROLE_ADMIN", "ROLE_PARENT"})
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, HttpServletRequest request) {
		if (studentRepository.existsById(id) && studentService.isActive(id)) {
			Principal principal = request.getUserPrincipal();
			if ((studentRepository.findById(id).get().getParent() != null &&
					!principal.getName().equals(studentRepository.findById(id).get().getParent().getUsername()))
					&& !adminRepository.existsByUsername(principal.getName())) {
				throw new AuthorizationServiceException("Forbidden");
			}
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
		logger.info("Added student: " + newStudent.toString());
		return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
	}
	
	//	Izmeni ucenika
	@Secured("ROLE_ADMIN")
	@PutMapping(value = "/{studentId}")
	public ResponseEntity<?> updateStudent(@PathVariable Integer studentId, @Valid @RequestBody StudentDto ustudent, 
			BindingResult result) {
		if(studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			if (result.hasErrors()) {
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}
			StudentEntity student = studentRepository.findById(studentId).get();
			student.setFirstName(ustudent.getFirstName());
			student.setLastName(ustudent.getLastName());
			studentRepository.save(student);
			logger.info("Updated student with ID: " + studentId.toString());
			return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
	}
	
	//	Obrisi ucenika
	@Secured("ROLE_ADMIN")
	@DeleteMapping(value = "/{studentId}")
	public ResponseEntity<?> deleteStudent(@PathVariable Integer studentId) {
		if(studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			StudentEntity student = studentRepository.findById(studentId).get();
			student.setDeleted(true);
			studentRepository.save(student);
			logger.info("Deleted student with ID: " + studentId.toString());
			return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
	}

	// Dodaj odeljenje za ucenika
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/{studentId}/class/{classId}")
	public ResponseEntity<?> addClass(@PathVariable Integer studentId, @PathVariable Integer classId) {
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			if (classRepository.existsById(classId) && classService.isActive(classId)) {
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
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			if (classRepository.existsById(classId) && classService.isActive(classId)) {
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
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			if (parentRepository.existsById(parentId) && parentService.isActive(parentId)) {
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
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			if (courseRepository.existsById(courseId) && courseService.isActive(courseId)) {
				if (teacherRepository.existsById(teacherId) && teacherService.isActive(teacherId)) {
					TeacherEntity teacher = teacherRepository.findById(teacherId).get();
					CourseEntity course = courseRepository.findById(courseId).get();
					if (teacherCourseRepository.existsByTeacherAndCourse(teacher, course)
							&& teacherCourseService.isActive(teacherCourseRepository.findByTeacherAndCourse(teacher, course).getId())) {
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
	
	//	Vrati sve predmete za ucenika
	@Secured({"ROLE_ADMIN", "ROLE_PARENT", "ROLE_STUDENT"})
	@GetMapping(value = "/{studentId}/courses/")
	public ResponseEntity<?> getCoursesForStudent(@PathVariable Integer studentId, HttpServletRequest request) {
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			Principal principal = request.getUserPrincipal();
			if ((studentRepository.findById(studentId).get().getParent() != null &&
					!principal.getName().equals(studentRepository.findById(studentId).get().getParent().getUsername()))
					&& !adminRepository.existsByUsername(principal.getName())
					&& !principal.getName().equals(studentRepository.findById(studentId).get().getUsername())) {
				throw new AuthorizationServiceException("Forbidden");
			}
			return new ResponseEntity<List<TeacherCourseEntity>>(studentService.getCourses(studentId), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
	}
	
	//	Obrisi predmet za ucenika
	@Secured("ROLE_ADMIN")
	@DeleteMapping(value = "/{studentId}/courses/{tcId}")
	public ResponseEntity<?> deleteTeacherCourseForStudent(@PathVariable Integer studentId, @PathVariable Integer tcId) {
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			if (teacherCourseRepository.existsById(tcId) && teacherCourseService.isActive(tcId)) {
				StudentEntity student = studentRepository.findById(studentId).get();
				TeacherCourseEntity teacherCourse = teacherCourseRepository.findById(tcId).get();
				if (studentTeacherCourseRepository.existsByStudentAndTeacherCourse(student, teacherCourse)
						&& studentTeacherCourseService
						.isActive(studentTeacherCourseRepository
								.findByStudentAndTeacherCourse(student, teacherCourse).getId())) {
					StudentTeacherCourseEntity stce = studentTeacherCourseRepository.findByStudentAndTeacherCourse(student, teacherCourse);
					stce.setDeleted(true);
					studentTeacherCourseRepository.save(stce);
					return new ResponseEntity<StudentTeacherCourseEntity>(stce, HttpStatus.OK);
					
				}
				return new ResponseEntity<RESTError>(new RESTError(15, "Student does not attend that course."), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError(11, "Teacher course combination not found."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(5, "Student not found."), HttpStatus.NOT_FOUND);
	}
	
	//	Vrati student-teacher-course kombinaciju
	@Secured({"ROLE_ADMIN", "ROLE_PARENT", "ROLE_STUDENT"})
	@GetMapping(value = "/{studentId}/teacher-course/{teacherCourseId}")
	public ResponseEntity<?> getStudentTeacherCourse(@PathVariable Integer studentId, @PathVariable Integer teacherCourseId, 
			HttpServletRequest request) {
		if (studentRepository.existsById(studentId) && studentService.isActive(studentId)) {
			if (teacherCourseRepository.existsById(teacherCourseId) && teacherCourseService.isActive(teacherCourseId)) {
				Principal principal = request.getUserPrincipal();
				if ((studentRepository.findById(studentId).get().getParent() != null &&
						!principal.getName().equals(studentRepository.findById(studentId).get().getParent().getUsername()))
						&& !adminRepository.existsByUsername(principal.getName())
						&& !principal.getName().equals(studentRepository.findById(studentId).get().getUsername())) {
					throw new AuthorizationServiceException("Forbidden");
				}
				StudentEntity student = studentRepository.findById(studentId).get();
				TeacherCourseEntity teacherCourse = teacherCourseRepository.findById(teacherCourseId).get();
				StudentTeacherCourseEntity stce = studentTeacherCourseRepository.findByStudentAndTeacherCourse(student, teacherCourse);
				if (stce != null && studentTeacherCourseService.isActive(stce.getId())) {
					return new ResponseEntity<StudentTeacherCourseEntity>(stce, HttpStatus.OK);
				}
				return new ResponseEntity<RESTError>(new RESTError(15, "Student does not attend that course."), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError(11, "Teacher course combination not found."),
					HttpStatus.NOT_FOUND);
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
