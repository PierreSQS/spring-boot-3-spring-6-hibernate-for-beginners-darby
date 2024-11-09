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

			addNewCourseToInstructor(appDAO);

			// deleteCourse(appDAO);

		};
	}

	private void deleteCourse(AppDAO appDAO) {
		int instructID = 12;
		log.info("Course with ID {}", appDAO.findCourseByID(instructID));
		log.info("Deleting Course ...");
		appDAO.deleteCourseByID(instructID);
		log.info("Course with ID {}", appDAO.findCourseByID(instructID));

	}


	private void addNewCourseToInstructor(AppDAO appDAO) {
		int instructID = 1;

		// find Instructor with ID 1
		log.info("Finding Instructor with the ID {}",instructID);
		Instructor foundInstructor = appDAO.findInstructorByIdJoinFetch(instructID);
		log.info("found Instructor: {}", foundInstructor);

		log.info("Creating new Course...");
		Course course = new Course();
		course.setTitle("New Course Title");

		// associate the course with find Instructor
		log.info("Adding course to Instructor...");
		foundInstructor.getCourses().add(course);
		course.setInstructor(foundInstructor);

		// update the Instructor
		appDAO.updateInstructor(foundInstructor);

		// show the all the Instructor's Courses
		log.info("All Instructor's Courses: {}",appDAO.findInstructorById(instructID).getCourses());

		printDoneMessage();

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
		Instructor instructor = new Instructor("Pierrot","Mongonnam",
				"pierrot.mongonnam@luv2code.com");

		// create the instructor detail
		InstructorDetail instructorDetail =
				new InstructorDetail(
						"http://www.youtube.com/mongonnam",
						"Travels");

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

		printDoneMessage();
	}
}








