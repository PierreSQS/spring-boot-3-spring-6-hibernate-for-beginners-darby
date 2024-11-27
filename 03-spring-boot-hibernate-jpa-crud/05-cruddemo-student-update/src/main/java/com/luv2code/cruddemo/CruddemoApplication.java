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

		return runner -> updateStudent(studentDAO);
	}

	private void updateStudent(StudentDAO studentDAO) {

		// retrieve student based on the id: primary key
		int studentId = 5;
        log.info("Getting student with id: {}", studentId);
		Student foundStudent = studentDAO.findStudentByID(studentId);
		log.info("Displaying the found student: {}", foundStudent);

		// change first name to "John"
		log.info("Updating the found student ...");
		foundStudent.setFirstName("John");
		foundStudent.setEmail("john.lucas@example.com");

		// update the student
		studentDAO.updateStudent(foundStudent);

		// display the updated student
        log.info("Updated student: {}", foundStudent);
	}


}







