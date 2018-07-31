package com.example.final_project_test.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.final_project_test.entities.AdminEntity;
import com.example.final_project_test.entities.ParentEntity;
import com.example.final_project_test.entities.RoleEntity;
import com.example.final_project_test.entities.StudentEntity;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.repositories.AdminRepository;
import com.example.final_project_test.repositories.ParentRepository;
import com.example.final_project_test.repositories.RoleRepository;
import com.example.final_project_test.repositories.StudentRepository;
import com.example.final_project_test.repositories.TeacherRepository;

@Component
public class DatabaseInit {
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private ParentRepository parentRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private RoleEntity adminRole;
	private RoleEntity teacherRole;
	private RoleEntity studentRole;
	private RoleEntity parentRole;
	
	@PostConstruct
	public void init() {
		roleInit();
		userInit();
	}
	
	private void roleInit() {
		if (((List<RoleEntity>) roleRepository.findAll()).isEmpty()) {
			adminRole = new RoleEntity("ROLE_ADMIN");
			teacherRole = new RoleEntity("ROLE_TEACHER");
			studentRole = new RoleEntity("ROLE_STUDENT");
			parentRole = new RoleEntity("ROLE_PARENT");
			roleRepository.save(adminRole);
			roleRepository.save(teacherRole);
			roleRepository.save(studentRole);
			roleRepository.save(parentRole);
		} else {
			adminRole = roleRepository.findByName("ROLE_ADMIN");
			teacherRole = roleRepository.findByName("ROLE_TEACHER");
			studentRole = roleRepository.findByName("ROLE_STUDENT");
			parentRole = roleRepository.findByName("ROLE_PARENT");
		}
		
	}
	
	private void userInit() {
		if (((List<AdminEntity>) adminRepository.findAll()).isEmpty()) {
			AdminEntity admin = new AdminEntity();
			admin.setDeleted(false);
			admin.setFirstName("admin");
			admin.setLastName("admin");
			admin.setUsername("admin");
			admin.setPassword(Encryption.getPassEncoded("admin"));
			admin.setRole(adminRole);
			adminRepository.save(admin);
		}
		if (((List<TeacherEntity>) teacherRepository.findAll()).isEmpty()) {
			TeacherEntity teacher = new TeacherEntity();
			teacher.setDeleted(false);
			teacher.setFirstName("teacher");
			teacher.setLastName("teacher");
			teacher.setUsername("teacher");
			teacher.setPassword(Encryption.getPassEncoded("teacher"));
			teacher.setRole(teacherRole);
			teacherRepository.save(teacher);
		}
		if (((List<StudentEntity>) studentRepository.findAll()).isEmpty()) {
			StudentEntity student = new StudentEntity();
			student.setDeleted(false);
			student.setFirstName("student");
			student.setLastName("student");
			student.setUsername("student");
			student.setPassword(Encryption.getPassEncoded("student"));
			student.setRole(studentRole);
			studentRepository.save(student);
		}
		if (((List<ParentEntity>) parentRepository.findAll()).isEmpty()) {
			ParentEntity parent = new ParentEntity();
			parent.setDeleted(false);
			parent.setFirstName("parent");
			parent.setLastName("parent");
			parent.setUsername("parent");
			parent.setEmail("brains.ednevnik@gmail.com");
			parent.setPassword(Encryption.getPassEncoded("parent"));
			parent.setRole(parentRole);
			parentRepository.save(parent);
		}
	}

}
