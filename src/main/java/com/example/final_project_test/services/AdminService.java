package com.example.final_project_test.services;

import java.util.List;

public interface AdminService {
	
	List<String> getAllUsernames();
	
	Integer findIdByUsername(String username);

}
