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

@Slf4j
@SpringBootApplication
public class CruddemoApplication {

	public static final String DONE_MSG = "### Done! ###";
	public static final String THE_ASSOCIATED_COURSES = "### The associated courses: ";

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

			// findCoursesForInstructor(appDAO);

			// findInstructorWithCoursesJoinFetch(appDAO);

			updateInstructor(appDAO);

		};
	}

	private void updateInstructor(AppDAO appDAO) {

		int theId = 1;

		// find the instructor
		findingInstructorMsg(theId);
		Instructor tempInstructor = appDAO.findInstructorById(theId);

		// update the instructor
		processingMsg("### Updating instructor with ID: " + theId);
		tempInstructor.setLastName("TESTER");

		appDAO.update(tempInstructor);

		processingMsg(DONE_MSG);
	}

	private static void findingInstructorMsg(int theId) {
		log.info("### Finding instructor with ID: {} ", theId);
	}

	private void findInstructorWithCoursesJoinFetch(AppDAO appDAO) {

		int theId = 1;

		// find the instructor
		findingInstructorMsg(theId);
		Instructor foundInstructor = appDAO.findInstructorByIdJoinFetch(theId);

        log.info("foundInstructor: {}", foundInstructor);
		processingMsg(THE_ASSOCIATED_COURSES + foundInstructor.getCourses());

		processingMsg(DONE_MSG);
	}

	private void findCoursesForInstructor(AppDAO appDAO) {

		int theId = 1;
		// find instructor
		findingInstructorMsg(theId);

		Instructor foundInstructor = appDAO.findInstructorById(theId);

		processingMsg("### Found Instructor: " + foundInstructor);

		// find courses for instructor
		processingMsg("### Finding courses for instructor id: " + theId);
		List<Course> courses = appDAO.findCoursesByInstructorId(theId);

		// associate the objects
		foundInstructor.setCourses(new HashSet<>(courses));

		processingMsg(THE_ASSOCIATED_COURSES + foundInstructor.getCourses());

		processingMsg(DONE_MSG);
	}

	private void findInstructorWithCourses(AppDAO appDAO) {

		int theId = 1;
		findingInstructorMsg(theId);

		Instructor foundInstructor = appDAO.findInstructorById(theId);

		processingMsg("### Found Instructor: " + foundInstructor);
		processingMsg(THE_ASSOCIATED_COURSES + foundInstructor.getCourses());

		processingMsg(DONE_MSG);
	}

	private void createInstructorWithCourses(AppDAO appDAO) {

		// create the instructor
		Instructor instrToCreate = Instructor.builder()
				.firstName("Susan")
				.lastName("Public")
				.email("susan.public@luv2code.com").build();

		// create the instructor detail
		InstructorDetail instrDetailToCreate = InstructorDetail.builder()
				.youtubeChannel("http://www.youtube.com")
				.hobby("Video Games")
				.build();

		// associate the objects
		instrToCreate.setInstructorDetail(instrDetailToCreate);

		// create some courses
		Course tempCourse1 = Course.builder()
				.title("Air Guitar - The Ultimate Guide")
				.build();

		Course tempCourse2 = Course.builder()
				.title("The Pinball Masterclass")
				.build();

		// add courses to instructor
		instrToCreate.add(tempCourse1);
		instrToCreate.add(tempCourse2);

		// save the instructor
		//
		// NOTE: this will ALSO save the courses
		// because of CascadeType.PERSIST
		//
		processingMsg("### Saving instructor: " + instrToCreate);
		processingMsg("### The courses: " + instrToCreate.getCourses());
		appDAO.save(instrToCreate);

		processingMsg(DONE_MSG);
	}

	private static void processingMsg(String instrToCreate) {
		log.info(instrToCreate);
	}

	private void deleteInstructorDetail(AppDAO appDAO) {

		int theId = 3;
		processingMsg("### Deleting instructor detail ID: " + theId);

		appDAO.deleteInstructorDetailById(theId);

		processingMsg(DONE_MSG);
	}

	private void findInstructorDetail(AppDAO appDAO) {

		// get the instructor detail object
		int theId = 2;
		InstructorDetail foundInstrDetail = appDAO.findInstructorDetailById(theId);

		// print the instructor detail
		processingMsg("### Found InstrDetail: " + foundInstrDetail);

		// print the associated instructor
		processingMsg("### The associated instructor: " + foundInstrDetail.getInstructor());

		processingMsg(DONE_MSG);
	}

	private void deleteInstructor(AppDAO appDAO) {

		int theId = 1;
		processingMsg("Deleting instructor id: " + theId);

		appDAO.deleteInstructorById(theId);

		processingMsg(DONE_MSG);
	}

	private void findInstructor(AppDAO appDAO) {

		int theId = 2;
		findingInstructorMsg(theId);

		Instructor tempInstructor = appDAO.findInstructorById(theId);

		processingMsg("tempInstructor: " + tempInstructor);
		processingMsg("the associated instructorDetail only: " + tempInstructor.getInstructorDetail());

	}

	private void createInstructor(AppDAO appDAO) {

		// create the instructor
		Instructor instructorToCreate = Instructor.builder()
				.firstName("Madhu")
				.lastName("Patel")
				.email("madhu@luv2code.com")
				.build();

		// create the instructor detail
		InstructorDetail instrDetailToCreate = InstructorDetail.builder()
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
		processingMsg("Saving instructor: " + instructorToCreate);
		appDAO.save(instructorToCreate);

		processingMsg(DONE_MSG);
	}
}








