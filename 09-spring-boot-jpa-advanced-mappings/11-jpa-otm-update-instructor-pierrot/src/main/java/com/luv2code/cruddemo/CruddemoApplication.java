package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.AppDAO;
import com.luv2code.cruddemo.entity.Course;
import com.luv2code.cruddemo.entity.Instructor;
import com.luv2code.cruddemo.entity.InstructorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@Slf4j
@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AppDAO appDAO) {

		return runner -> {

			// createInstructorWithCourses(appDAO);

			findInstructorByIdJoinFetch(appDAO);

		};
	}

	private void findInstructorByIdJoinFetch(AppDAO appDAO) {
		int instructID = 1;

		log.info("Finding The Instructor by ID with JoinFetch: {}", instructID);
		Instructor instructorById = appDAO.findInstructorByIdJoinFetch(instructID);

		log.info("The found instructor: {}", instructorById);

		log.info("The courses of the Instructor: {}",instructorById.getCourses());

		log.info("Done!");
	}

	private void createInstructorWithCourses(AppDAO dao) {
		// create instructor
		Instructor instructor = new Instructor("Pierrot","Mongonamm",
				"pierrot.mongonnam@luv2code.com");

		// create the instructor detail
		InstructorDetail instructorDetail =
				new InstructorDetail(
						"http://www.youtube.com/mongonnam",
						"Video Games");

		// create instructor courses
		Course course1 = new Course("SpringBoot 3");
		Course course2 = new Course("Junit5");

		// associate the objects
		instructor.addCourses(Set.of(course1, course2));
		instructor.setInstructorDetail(instructorDetail);

		// save the instructor
		//
		// NOTE: this will ALSO save the courses
		// because of CascadeType.PERSIST
		//
		log.info("Saving instructor: {}", instructor);
		log.info("The courses: {}", instructor.getCourses());
		dao.save(instructor);

		log.info("Done!");
	}
}








