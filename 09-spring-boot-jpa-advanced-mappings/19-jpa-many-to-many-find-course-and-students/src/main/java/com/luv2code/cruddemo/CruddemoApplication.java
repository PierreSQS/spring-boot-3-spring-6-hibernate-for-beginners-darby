package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.AppDAO;
import com.luv2code.cruddemo.entity.Course;
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

			findCoursesAndStudents(appDAO);

		};
	}

	private void findCoursesAndStudents(AppDAO appDAO) {
		int courseID = 10;

		// finding course and students by courseID
		log.info("Finding course by ID and students...");
		Course foundCourse = appDAO.findCourseAndReviewsByCourseId(courseID);


		// list the Students to Course
		log.info("Listing the Students of course with the ID: {}...", courseID);

		log.info("The students in the course {}", foundCourse.getStudents());


		printDoneMessage();
	}

	private static void printDoneMessage() {
		log.info("Done!");
	}

}








