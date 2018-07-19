package com.example.final_project_test.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{
	
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

}
