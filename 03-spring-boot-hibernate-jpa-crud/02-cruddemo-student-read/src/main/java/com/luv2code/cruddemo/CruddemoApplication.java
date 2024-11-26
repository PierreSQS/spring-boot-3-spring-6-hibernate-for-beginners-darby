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

		return runner -> readStudent(studentDAO);
	}

	private void readStudent(StudentDAO studentDAO) {

		// create  a createdStudent object
		log.info("Creating a Student for findStudent...");
		Student createdStudent = Student
				.builder()
				.firstName("Paul")
				.lastName("Lucas")
				.email("paul.lucas@example.com")
				.build();

		// save the createdStudent
		log.info("Saving the new created createdStudent {}", createdStudent);
		studentDAO.save(createdStudent);
		// display the saved createdStudent with id
		log.info("Displaying the saved createdStudent with ID: {} ",createdStudent);

		// find createdStudent based on the id: primary key
		int savedStudentID = createdStudent.getId();
		log.info("Finding the createdStudent by ID: {}", savedStudentID);
		Student foundStudent = studentDAO.findStudentByID(savedStudentID);

		// display the found createdStudent
		log.info("Displaying the found student by ID: {} ",foundStudent);

		// display the found createdStudent
		log.info("Displaying the created student again: {} ",foundStudent);

	}

}







