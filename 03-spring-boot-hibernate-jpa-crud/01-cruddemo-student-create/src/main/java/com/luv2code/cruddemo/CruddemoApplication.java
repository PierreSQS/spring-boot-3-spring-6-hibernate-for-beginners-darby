package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.StudentDAO;
import com.luv2code.cruddemo.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Slf4j
@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO) {

		return runner -> {
			createStudent(studentDAO);
			createMultipleStudents(studentDAO);
		};
	}

	private void createMultipleStudents(StudentDAO studentDAO) {
		// create students
		log.info("Creating 3 Students...");
		Student studentToCreate1 = Student
				.builder()
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@luv2code.com")
				.build();

		Student studentToCreate2 = Student
				.builder()
				.firstName("Mary")
				.lastName("Public")
				.email("john.doe@luv2code.com")
				.build();

		Student studentToCreate3 = Student
				.builder()
				.firstName("Bonita")
				.lastName("Applebaum")
				.email("bonita.applebaum@luv2code.com")
				.build();

		List<Student> students = List.of(studentToCreate1, studentToCreate2, studentToCreate3);


		// save the new created students
		log.info("Saving the new created students...");
		students.forEach(
				studentDAO::save
		);

		// Display the new created Student
		log.info("Displaying the new created students {}",students);

		// Done message
		doneMessage();
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

		// Done message
		doneMessage();

	}

	private static void doneMessage() {
		log.info("****** Done! ******");
	}

}






