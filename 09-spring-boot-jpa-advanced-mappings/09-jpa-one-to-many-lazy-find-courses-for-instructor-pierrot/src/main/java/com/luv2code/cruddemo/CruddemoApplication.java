package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.AppDAO;
import com.luv2code.cruddemo.entity.Course;
import com.luv2code.cruddemo.entity.Instructor;
import com.luv2code.cruddemo.entity.InstructorDetail;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

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

			findCoursesByInstructorID(appDAO);

		};
	}

	private void findCoursesByInstructorID(AppDAO appDAO) {
		int instructID = 1;

		System.out.println("Finding the Instructor with the ID: "+instructID);
		Instructor instructorById = appDAO.findInstructorById(instructID);
		System.out.println("The found instructor: "+instructorById);

		System.out.println("Finding the Courses of the Instructor with the ID: "+instructID);
		List<Course> coursesByInstructorID = appDAO.findCoursesByInstructorID(instructID);
		System.out.println("The courses of the instructor: "+coursesByInstructorID);

		System.out.println("Getting the associated courses of the Instructor with the ID: "+instructID);
		Set<Course> associatesCourses = instructorById.getCourses();// should raise exception since lazy loading
		System.out.println("the associated courses: "+associatesCourses);

		System.out.println("Done!");
	}

	private void findInstructorWithCourses(AppDAO appDAO) {
		int instructID = 1;

		System.out.println("Finding the Instructor with the ID: "+instructID);
		Instructor instructorById = appDAO.findInstructorById(instructID);

		System.out.println("Found Intructor: "+instructorById);
		System.out.println("Intructor Courses: "+instructorById.getCourses());

		System.out.println("Done!");
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
		System.out.println("Saving instructor: " + instructor);
		System.out.println("The courses: " + instructor.getCourses());
		dao.save(instructor);

		System.out.println("Done!");
	}

	private void deleteInstructorDetail(AppDAO appDAO) {

		int theId = 3;
		System.out.println("Deleting instructor detail id: " + theId);

		appDAO.deleteInstructorDetailById(theId);

		System.out.println("Done!");
	}

	private void findInstructorDetail(AppDAO appDAO) {

		// get the instructor detail object
		int theId = 2;
		InstructorDetail tempInstructorDetail = appDAO.findInstructorDetailById(theId);

		// print the instructor detail
		System.out.println("tempInstructorDetail: " + tempInstructorDetail);

		// print the associated instructor
		System.out.println("the associated instructor: " + tempInstructorDetail.getInstructor());

		System.out.println("Done!");
	}

	private void deleteInstructor(AppDAO appDAO) {

		int theId = 1;
		System.out.println("Deleting instructor id: " + theId);

		appDAO.deleteInstructorById(theId);

		System.out.println("Done!");
	}

	private void findInstructor(AppDAO appDAO) {

		int theId = 2;
		System.out.println("Finding instructor id: " + theId);

		Instructor tempInstructor = appDAO.findInstructorById(theId);

		System.out.println("tempInstructor: " + tempInstructor);
		System.out.println("the associated instructorDetail only: " + tempInstructor.getInstructorDetail());

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
		System.out.println("Saving instructor: " + tempInstructor);
		appDAO.save(tempInstructor);

		System.out.println("Done!");
	}
}








