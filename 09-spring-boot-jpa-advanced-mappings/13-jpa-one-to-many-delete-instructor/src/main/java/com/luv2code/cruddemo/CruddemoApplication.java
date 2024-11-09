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

			createInstructorWithCourses(appDAO);

			deleteInstructor(appDAO);

		};
	}

	private void deleteInstructor(AppDAO appDAO) {
		int instructID = 2;

		log.info("Finding Instructor with the ID {}",instructID);
		Instructor foundInstructor = appDAO.findInstructorById(instructID);
		log.info("found Instructor: {}", foundInstructor);

		log.info("Deleting the Instructor...");
		appDAO.deleteInstructorById(instructID);
		log.info("Finding the deleted Instructor: {}", appDAO.findInstructorById(instructID));

		printDoneMessage();

	}

	private static void printDoneMessage() {
		log.info("Done!");
	}

	private void createInstructorWithCourses(AppDAO dao) {
		// create instructor
		Instructor instructor = new Instructor("Pierrot","ToDelete",
				"pierrot.todelete@luv2code.com");

		// create the instructor detail
		InstructorDetail instructorDetail =
				new InstructorDetail(
						"http://www.youtube.com/todelete",
						"Travels");

		// create instructor courses
		Course course1 = new Course("Java 21");
		Course course2 = new Course("Spring6");

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

		printDoneMessage();
	}
}








