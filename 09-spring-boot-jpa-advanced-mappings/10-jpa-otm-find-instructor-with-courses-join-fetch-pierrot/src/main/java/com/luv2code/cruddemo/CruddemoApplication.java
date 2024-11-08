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

		return runner -> {
			// createInstructor(appDAO);

			// findInstructor(appDAO);

			// deleteInstructor(appDAO);

			// findInstructorDetail(appDAO);

			// deleteInstructorDetail(appDAO);

			// createInstructorWithCourses(appDAO);

			// findInstructorWithCourses(appDAO);

			// findCoursesByInstructorID(appDAO);

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

	private void findCoursesByInstructorID(AppDAO appDAO) {
		int instructID = 1;

        log.info("Finding the Instructor with the ID: {}", instructID);
		Instructor instructorById = appDAO.findInstructorById(instructID);
        log.info("The found instructor: {}", instructorById);

        log.info("Finding the Courses of the Instructor with the ID: {}", instructID);
		List<Course> coursesByInstructorID = appDAO.findCoursesByInstructorID(instructID);
        log.info("The courses of the instructor: {}", coursesByInstructorID);

		// workaround for the lazy loading issue in the associated courses
		instructorById.setCourses(new HashSet<>(coursesByInstructorID));

        log.info("Getting the associated courses of the Instructor with the ID: {}", instructID);
		Set<Course> associatesCourses = instructorById.getCourses();// should raise exception since lazy loading
        log.info("the associated courses: {}", associatesCourses);

		log.info("Done!");
	}

	private void findInstructorWithCourses(AppDAO appDAO) {
		int instructID = 1;

        log.info("Finding the Instructor with the ID: {}", instructID);
		Instructor instructorById = appDAO.findInstructorById(instructID);

        log.info("Found Intructor: {}", instructorById);
        log.info("Intructor Courses: {}", instructorById.getCourses());

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

	private void deleteInstructorDetail(AppDAO appDAO) {

		int theId = 3;
        log.info("Deleting instructor detail id: {}", theId);

		appDAO.deleteInstructorDetailById(theId);

		log.info("Done!");
	}

	private void findInstructorDetail(AppDAO appDAO) {

		// get the instructor detail object
		int theId = 2;
		InstructorDetail tempInstructorDetail = appDAO.findInstructorDetailById(theId);

		// print the instructor detail
        log.info("tempInstructorDetail: {}", tempInstructorDetail);

		// print the associated instructor
        log.info("the associated instructor: {}", tempInstructorDetail.getInstructor());

		log.info("Done!");
	}

	private void deleteInstructor(AppDAO appDAO) {

		int theId = 1;
        log.info("Deleting instructor id: {}", theId);

		appDAO.deleteInstructorById(theId);

		log.info("Done!");
	}

	private void findInstructor(AppDAO appDAO) {

		int theId = 2;
        log.info("Finding instructor id: {}", theId);

		Instructor tempInstructor = appDAO.findInstructorById(theId);

        log.info("tempInstructor: {}", tempInstructor);
        log.info("the associated instructorDetail only: {}", tempInstructor.getInstructorDetail());

	}

	private void createInstructor(AppDAO appDAO) {
		// create the instructor
		Instructor tempInstructor =
				new Instructor("Madhu", "Patel", "madhu@luv2code.com");

		// create the instructor detail
		InstructorDetail tempInstructorDetail =
				new InstructorDetail(
						"http://www.luv2code.com/youtube",
						"Guitar");

		// associate the objects
		tempInstructor.setInstructorDetail(tempInstructorDetail);

		// save the instructor
		//
		// NOTE: this will ALSO save the details object
		// because of CascadeType.ALL
		//
        log.info("Saving instructor: {}", tempInstructor);
		appDAO.save(tempInstructor);

		log.info("Done!");
	}
}








