package com.example.final_project_test.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project_test.services.AdminService;


@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthController {
	
	@Autowired
	private AdminService adminService;

	@PostMapping(value = "login")
	public ResponseEntity<Object> login() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String role = auth.getAuthorities().iterator().next().getAuthority();
		return new ResponseEntity<>("{\"role\":\"" + role + "\"}", HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "usernames")
	public ResponseEntity<List<String>> getAllUsernames() {
		return new ResponseEntity<List<String>>(this.adminService.getAllUsernames(), HttpStatus.OK);
	}

}
