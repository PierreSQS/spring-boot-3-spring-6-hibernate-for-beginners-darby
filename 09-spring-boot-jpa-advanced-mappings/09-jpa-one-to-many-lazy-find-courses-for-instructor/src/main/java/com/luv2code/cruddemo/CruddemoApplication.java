package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.AppDAO;
import com.luv2code.cruddemo.entity.Course;
import com.luv2code.cruddemo.entity.Instructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AppDAO appDAO) {

		return runner -> findCoursesForInstructor(appDAO);
	}

	private void findCoursesForInstructor(AppDAO appDAO) {

		int instructID = 1;
		// find instructor
		log.info("### Finding the Instructor with the ID: {}", instructID);

		Instructor instructorById = appDAO.findInstructorById(instructID);

		log.info("### The found instructor: {}", instructorById);

		// find courses for instructor
		log.info("### Finding the Courses of the Instructor with the ID: {}", instructID);
		List<Course> coursesByInstructorID = appDAO.findCoursesByInstructorId(instructID);
		log.info("### The founded courses of the instructor: {}", coursesByInstructorID);

		// WE MUST ASSOCIATE THE OBJECTS, SINCE LAZY LOADING !!!
		instructorById.setCourses(new HashSet<>(coursesByInstructorID));

		log.info("### Getting the associated courses of the Instructor with the ID: {}", instructID);
		// RAISES AN EXCEPTION WITHOUT THE PREVIOUS STATEMENT SINCE LAZY LOADING
		Set<Course> associatesCourses = instructorById.getCourses();
		log.info("### the associated courses: {}", associatesCourses);

		log.info("Done!");
	}

}








