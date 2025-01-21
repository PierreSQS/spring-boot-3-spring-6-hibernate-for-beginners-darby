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

			findInstructorWithCourses(appDAO);

		};
	}

	private void findInstructorWithCourses(AppDAO appDAO) {

		int theId = 1;
		log.info("### Finding instructor with id: {}... ###", theId);

		Instructor foundInstructor = appDAO.findInstructorById(theId);

        log.info("### Found Instructor: {} ###", foundInstructor);

		// CALLING THE COURSES OF THE INSTRUCTOR WILL NOT WORK AT THIS STAGE!!!!
		// SINCE LAZY LOADING
		log.info("### The associated courses from Instructor object: {} ###", foundInstructor.getCourses());

		displayDoneMsg();
	}

	private void createInstructorWithCourses(AppDAO appDAO) {

		// create the instructor
		Instructor instructorToCreate = Instructor.builder()
				.firstName("Susan")
				.lastName("Public")
				.email("susan.public@luv2code.com")
				.build();

		// create the instructor detail
		InstructorDetail tempInstructorDetail = InstructorDetail.builder()
				.youtubeChannel("http://www.youtube.com")
				.hobby("Video Games").build();

		// associate the objects
		instructorToCreate.setInstructorDetail(tempInstructorDetail);

		// create some courses
		Course tempCourse1 = Course.builder()
				.title("Air Guitar - The Ultimate Guide")
				.build();

		Course tempCourse2 = Course.builder()
				.title("The Pinball Masterclass")
				.build();

		// add courses to instructor
		instructorToCreate.add(tempCourse1);
		instructorToCreate.add(tempCourse2);

		// save the instructor
		//
		// NOTE: this will ALSO save the courses
		// because of CascadeType.PERSIST
		//
		displaySavingInstructorMsg(instructorToCreate);
		log.info("The courses: {}", instructorToCreate.getCourses());
		appDAO.save(instructorToCreate);

		displayDoneMsg();
	}

	private static void displaySavingInstructorMsg(Instructor tempInstructor) {
        log.info("Saving instructor: {}", tempInstructor);
	}

	private static void displayDoneMsg() {
		log.info("Done!");
	}

	private void deleteInstructorDetail(AppDAO appDAO) {

		int theId = 3;
        log.info("Deleting instructor detail id: {}", theId);

		appDAO.deleteInstructorDetailById(theId);

		displayDoneMsg();
	}

	private void findInstructorDetail(AppDAO appDAO) {

		// get the instructor detail object
		int theId = 2;
		InstructorDetail tempInstructorDetail = appDAO.findInstructorDetailById(theId);

		// print the instructor detail
        log.info("tempInstructorDetail: {}", tempInstructorDetail);

		// print the associated instructor
        log.info("the associated instructor: {}", tempInstructorDetail.getInstructor());

		displayDoneMsg();
	}

	private void deleteInstructor(AppDAO appDAO) {

		int theId = 1;
        log.info("Deleting instructor id: {}", theId);

		appDAO.deleteInstructorById(theId);

		displayDoneMsg();
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
		Instructor instructorToCreate = Instructor.builder()
						.firstName("Madhu")
						.lastName("Patel")
						.email("madhu@luv2code.com")
						.build();

		// create the instructor detail
		InstructorDetail instrDetailToCreate =
				InstructorDetail.builder()
						.youtubeChannel("http://www.luv2code.com/youtube")
						.hobby("Guitar")
						.build();

		// associate the objects
		instructorToCreate.setInstructorDetail(instrDetailToCreate);

		// save the instructor
		//
		// NOTE: this will ALSO save the details object
		// because of CascadeType.ALL
		//
		displaySavingInstructorMsg(instructorToCreate);
		appDAO.save(instructorToCreate);

		displayDoneMsg();
	}
}








