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

		int theId = 1;
		// find instructor
        log.info("### Finding instructor with the ID: {}", theId);

		Instructor foundInstructor = appDAO.findInstructorById(theId);

        log.info("### the founded Instructor: {}", foundInstructor);

		// find courses for instructor
        log.info("### Finding courses for instructor with ID: {}", theId);
		List<Course> courses = appDAO.findCoursesByInstructorId(theId);

		// WE MUST ASSOCIATE THE OBJECTS, SINCE LAZY LOADING !!!
		foundInstructor.setCourses(new HashSet<>(courses));

        log.info("### the associated courses of the Instructor: {}", foundInstructor.getCourses());

		log.info("### Done! ###");
	}

}








