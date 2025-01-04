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
        log.info("Finding instructor id: {}", theId);

		Instructor tempInstructor = appDAO.findInstructorById(theId);

        log.info("tempInstructor: {}", tempInstructor);

		// find courses for instructor
        log.info("Finding courses for instructor id: {}", theId);
		List<Course> courses = appDAO.findCoursesByInstructorId(theId);

		// associate the objects
		tempInstructor.setCourses(new HashSet<>(courses));

        log.info("the associated courses: {}", tempInstructor.getCourses());

		log.info("Done!");
	}

}








