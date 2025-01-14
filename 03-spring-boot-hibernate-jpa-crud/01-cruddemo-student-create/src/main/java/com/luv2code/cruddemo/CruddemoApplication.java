package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.StudentDAO;
import com.luv2code.cruddemo.entity.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class CruddemoApplication implements CommandLineRunner {

	private final StudentDAO studentDAO;

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		createStudent(studentDAO);
		createMultipleStudents(studentDAO);
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


		// save the created new students
		log.info("Saving the created students...");
		students.forEach(
				studentDAO::save
		);

		// Display the created new Student
		log.info("Displaying the created students {}",students);

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

		// save the created new student
		log.info("Saving the new student...");
		studentDAO.save(studentToCreate);

		// Display the created new Student
		log.info("Displaying the created student {}",studentToCreate);

		// Done message
		doneMessage();

	}

	private static void doneMessage() {
		log.info("****** Done! ******");
	}

}






