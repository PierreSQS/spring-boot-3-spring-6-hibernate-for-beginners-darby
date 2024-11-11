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

		return runner -> findCourseAndReviewsByID(appDAO);
	}

	private void findCourseAndReviewsByID(AppDAO appDAO) {

		int courseID = 10;

		log.info("Finding Course with Reviews by ID: {}", courseID);
		Course foundCourse = appDAO.findCourseAndReviewsByCourseId(courseID);

		log.info("Found the Course: {}",foundCourse);
		log.info("The Review of the Course with ID {}: {}", foundCourse.getId(), foundCourse.getReviews());

		printDoneMessage();
	}

	private static void printDoneMessage() {
		log.info("Done!");
	}

}








