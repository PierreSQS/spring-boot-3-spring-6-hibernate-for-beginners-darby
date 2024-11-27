package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.StudentDAO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO) {

		return runner -> deleteStudent(studentDAO);
		
	}

	private void deleteStudent(StudentDAO studentDAO) {

		int studentID = 5;
		System.out.println("Deleting student id: " + studentID);
	}

	
}







