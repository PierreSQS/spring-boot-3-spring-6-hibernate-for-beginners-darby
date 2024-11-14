package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.AppDAO;
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
	public CommandLineRunner commandLineRunner(AppDAO appDAO) {

		return runner -> {

			findStudentAndCourses(appDAO);

		};
	}

	private void findStudentAndCourses(AppDAO appDAO) {
		int studentID = 1;

		// finding course and students by courseID
		log.info("Finding student by ID and courses...");
		Student foundStudent = appDAO.findStudentAndCoursesByStudentID(studentID);

		log.info("Found Student {}", foundStudent);


		// list the Students to Course
		log.info("Listing the courses of student with the ID: {}...", studentID);

		log.info("The courses of the student {}", foundStudent.getCourses());


		printDoneMessage();
	}

	private static void printDoneMessage() {
		log.info("Done!");
	}

}








