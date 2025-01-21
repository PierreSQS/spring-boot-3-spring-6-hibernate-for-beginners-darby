package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.AppDAO;
import com.luv2code.cruddemo.entity.Instructor;
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

		return runner -> findInstructorWithCoursesJoinFetch(appDAO);
	}

	private void findInstructorWithCoursesJoinFetch(AppDAO appDAO) {

		int theId = 1;

		// find the instructor
        log.info("### Finding instructor (and courses)  id: {}", theId);
		Instructor foundInstructor = appDAO.findInstructorByIdJoinFetch(theId);

		// WE DON'T NEED TO ASSOCIATE THE COURSES THE JOIN FETCH COMMAND
		// IN THE SQL-COMMAND, LOADED THE COURSES EAGERLY!!

        log.info("### Found Instructor: {}", foundInstructor);
        log.info("### The associated courses of the found instructor\n: {}", foundInstructor.getCourses());

		log.info("Done!");
	}

}








