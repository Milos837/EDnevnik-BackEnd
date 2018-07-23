package com.example.final_project_test.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.final_project_test.entities.AdminEntity;
import com.example.final_project_test.entities.ParentEntity;
import com.example.final_project_test.entities.StudentEntity;
import com.example.final_project_test.entities.TeacherEntity;
import com.example.final_project_test.repositories.AdminRepository;
import com.example.final_project_test.repositories.ParentRepository;
import com.example.final_project_test.repositories.StudentRepository;
import com.example.final_project_test.repositories.TeacherRepository;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private ParentRepository parentRepository;
	
	@PersistenceContext
	EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllUsernames() {
		String sql = "SELECT * FROM usernames";
		
		Query query = em.createNativeQuery(sql);
		
		List<String> result = new ArrayList<>();
		result = query.getResultList();
		
		return result;
	}
	
	@Override
	public Integer findIdByUsername(String username) {
		AdminEntity admin = adminRepository.findByUsername(username);
		ParentEntity parent = parentRepository.findByUsername(username);
		StudentEntity student = studentRepository.findByUsername(username);
		TeacherEntity teacher = teacherRepository.findByUsername(username);
		
		if(admin != null) {
			return admin.getId();
		}
		if(parent != null) {
			return parent.getId();
		}
		if(student != null) {
			return student.getId();
		}
		if(teacher != null) {
			return teacher.getId();
		}
		return null;
	}

}
