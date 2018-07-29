package com.example.final_project_test.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project_test.services.AdminService;
import com.example.final_project_test.services.FileHandler;


@RestController
@RequestMapping(value = "/api/v1/util/")
public class UtilController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FileHandler fileHandler;
	
	@Autowired
	private AdminService adminService;

	@PostMapping(value = "login")
	public ResponseEntity<Object> login() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String role = auth.getAuthorities().iterator().next().getAuthority();
		Integer id = adminService.findIdByUsername(auth.getName());
		logger.info("Log in request with username: " + auth.getName());
		if(id != null) {
			return new ResponseEntity<>("{\"role\":\"" + role + "\", \"id\":\"" + id + "\"}", HttpStatus.OK);
		}
		return new ResponseEntity<>("{\"role\":\"" + role + "\"}", HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "usernames")
	public ResponseEntity<List<String>> getAllUsernames() {
		return new ResponseEntity<List<String>>(this.adminService.getAllUsernames(), HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/download")
	public ResponseEntity<Resource> downloadLogs() {
		try {
			File file = fileHandler.getLogs();
			
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			
			HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.add("content-disposition", "attachment; filename=" + "logs.txt");
	        
			
			return ResponseEntity.ok()
		            .headers(responseHeaders)
		            .contentLength(file.length())
		            .contentType(MediaType.parseMediaType("application/octet-stream"))
		            .body(resource);
		} catch (IOException e) {
			e.getStackTrace();
		}
		return null;
	}

}
