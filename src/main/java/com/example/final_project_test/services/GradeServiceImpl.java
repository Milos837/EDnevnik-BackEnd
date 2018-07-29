package com.example.final_project_test.services;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.final_project_test.entities.CourseEntity;
import com.example.final_project_test.entities.GradeEntity;
import com.example.final_project_test.entities.StudentEntity;
import com.example.final_project_test.entities.StudentTeacherCourseEntity;
import com.example.final_project_test.entities.TeacherCourseEntity;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.entities.dto.GradeDto;
import com.example.final_project_test.entities.enums.EGradeType;
import com.example.final_project_test.entities.util.RESTError;
import com.example.final_project_test.repositories.CourseRepository;
import com.example.final_project_test.repositories.GradeRepository;
import com.example.final_project_test.repositories.StudentRepository;
import com.example.final_project_test.repositories.StudentTeacherCourseRepository;
import com.example.final_project_test.repositories.TeacherCourseRepository;
import com.example.final_project_test.repositories.TeacherRepository;

@Service
public class GradeServiceImpl implements GradeService{
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private TeacherCourseRepository teacherCourseRepository;
	
	@Autowired
	private StudentTeacherCourseRepository studentTeacherCourseRepository;
	
	@Autowired
	private GradeRepository gradeRepository;
	
	@Autowired
	public JavaMailSender emailSender;
	
	public ResponseEntity<?> gradeStudent(GradeDto newGrade, Integer studentId, Integer teacherId, Integer courseId) throws MessagingException {
		StudentEntity student = studentRepository.findById(studentId).get();
		TeacherEntity teacher = teacherRepository.findById(teacherId).get();
		CourseEntity course = courseRepository.findById(courseId).get();
		if(teacherCourseRepository.existsByTeacherAndCourse(teacher, course)) {
			TeacherCourseEntity teacherCourse = teacherCourseRepository.findByTeacherAndCourse(teacher, course);
			if(studentTeacherCourseRepository.existsByStudentAndTeacherCourse(student, teacherCourse)) {
				StudentTeacherCourseEntity stce = studentTeacherCourseRepository.findByStudentAndTeacherCourse(student, teacherCourse);
				if(!checkForFinalGrade(stce)) {
					GradeEntity grade = new GradeEntity();
					grade.setDeleted(false);
					grade.setStudentTeacherCourse(stce);
					grade.setValue(newGrade.getValue());
					grade.setType(newGrade.getType());
					grade.setDateUTC(ZonedDateTime.now(ZoneOffset.UTC));
					if(newGrade.getType().equals(EGradeType.FINAL)) {
						grade.setFinalGrade(true);
					} else {
						grade.setFinalGrade(false);
					}
					gradeRepository.save(grade);
					
					if(stce.getStudent().getParent().getEmail() != null) {
						sendEmailToParent(stce, grade);
					}
					
					return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
				}
				return new ResponseEntity<RESTError>(new RESTError(13, "Student already has final grade."), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError(12, "Student teacher course combination not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RESTError>(new RESTError(7, "Teacher doesn't teach this course."), HttpStatus.NOT_FOUND);
	}
	
	public ResponseEntity<?> gradeStudent(GradeDto newGrade, Integer studentTeacherCourse) throws MessagingException {
		StudentTeacherCourseEntity stce = studentTeacherCourseRepository.findById(studentTeacherCourse).get();
		if(!checkForFinalGrade(stce)) {
			GradeEntity grade = new GradeEntity();
			grade.setDeleted(false);
			grade.setStudentTeacherCourse(stce);
			grade.setValue(newGrade.getValue());
			grade.setType(newGrade.getType());
			grade.setDateUTC(ZonedDateTime.now(ZoneOffset.UTC));
			if(newGrade.getType().equals(EGradeType.FINAL)) {
				grade.setFinalGrade(true);
			} else {
				grade.setFinalGrade(false);
			}
			gradeRepository.save(grade);
			
			if(stce.getStudent().getParent() != null && stce.getStudent().getParent().getEmail() != null) {
				sendEmailToParent(stce, grade);
			}
			
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(13, "Student already has final grade."), HttpStatus.NOT_FOUND);
	}
	
	//	Proveri da li student ima zakljucnu ocenu iz predmeta kod nastavnika
	public Boolean checkForFinalGrade(StudentTeacherCourseEntity stce) {
		List<GradeEntity> grades = gradeRepository.findByStudentTeacherCourse(stce);
		for (GradeEntity gradeEntity : grades) {
			if(gradeEntity.getFinalGrade()) {
				return true;
			}
		}
		return false;
	}
	
	public void sendEmailToParent(StudentTeacherCourseEntity stc, GradeEntity grade) throws MessagingException {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(stc.getStudent().getParent().getEmail());
		helper.setSubject("New grade");
		String teacherName = stc.getTeacherCourse().getTeacher().getFirstName() + stc.getTeacherCourse().getTeacher().getLastName();
		String studentName = stc.getStudent().getFirstName() + stc.getStudent().getLastName();
		String table = "<html>\r\n" + 
				"<body>\r\n" + 
				"	<br><table border=\"4px\">\r\n" + 
				"		<tr>\r\n" + 
				"			<th>Teacher</th>\r\n" + 
				"			<th>Course</th>\r\n" + 
				"			<th>Grade</th>\r\n" + 
				"		</tr>\r\n" + 
				"		<tr>\r\n" + 
				"			<td>" + teacherName +"</td>\r\n" + 
				"			<td>" + stc.getTeacherCourse().getCourse().getName() + "</td>\r\n" + 
				"			<td>" + grade.getValue() + "</td>\r\n" + 
				"		</tr>\r\n" + 
				"	</table>\r\n" + 
				"</body>\r\n" + 
				"<br></html>";
		String text = "Greetings, your child " + studentName + " has received new grade:" + table + "Regards, school administration.";
		helper.setText(text, true);
		
		try {
			emailSender.send(mail);
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
