package com.example.final_project_test.services;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class FileHandlerImpl implements FileHandler{
	

	public File getLogs() {
		String path = "C:\\Java\\spring-workspace\\final_project_test\\logs\\spring-boot-logging.log";
		
		File log = new File(path);
		
		return log;
	}

}
