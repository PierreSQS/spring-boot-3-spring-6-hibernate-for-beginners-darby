package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.AppDAO;
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

			deleteCourse(appDAO);

		};
	}

	private void deleteCourse(AppDAO appDAO) {
		int instructID = 11;
		log.info("Finding and Deleting course with ID {}", instructID);
		appDAO.deleteCourseByID(instructID);
		log.info("Course with ID {}", appDAO.findCourseByID(instructID));

		printDoneMessage();

	}

	private static void printDoneMessage() {
		log.info("Done!");
	}

}








