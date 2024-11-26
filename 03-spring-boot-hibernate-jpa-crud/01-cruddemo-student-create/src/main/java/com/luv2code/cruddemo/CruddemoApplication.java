package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.StudentDAO;
import com.luv2code.cruddemo.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO) {

		return runner -> createStudent(studentDAO);
	}

	private void createStudent(StudentDAO studentDAO) {
		// create a student
		log.info("Creating a student...");
		Student studentToCreate = Student
				.builder()
				.firstName("Paul")
				.lastName("Doe")
				.email("paul.doe@luv2code.com")
				.build();
		log.info("The created Student {}", studentToCreate);

		// save the new created student
		log.info("Saving the new created student...");
		studentDAO.save(studentToCreate);

		// Display the new created Student
		log.info("Displaying the new created student {}",studentToCreate);

	}
}







