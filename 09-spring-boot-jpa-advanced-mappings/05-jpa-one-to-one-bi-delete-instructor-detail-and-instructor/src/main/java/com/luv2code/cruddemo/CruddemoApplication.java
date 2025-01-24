package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.AppDAO;
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

			deleteIntructorDetail(appDAO);
		};
	}

	private void deleteIntructorDetail(AppDAO appDAO) {
		// deleting the instructor detail object
		int theId = 1;
		log.info("### Deleting the InstructorDetail with {}... ###", theId);
		appDAO.deleteInstructorDetailById(theId);

		logFindingInstrDetailMsg(theId);
		Instructor foundInstructor = appDAO.findInstructorById(theId);

		if (foundInstructor == null) {
			// instructor detail not found
			log.info("### Instructor with ID: {} not found !###", theId);
		} else {
			// print the instructor detail
			log.info("### The found Instructor: {} ###", foundInstructor);

		}

		logEndMessage();
	}


	private void findInstructorDetail(AppDAO appDAO) {

		// get the instructor detail object
		int theId = 1;
		logFindingInstrDetailMsg(theId);
		InstructorDetail foundInstructorDetail = appDAO.findInstructorDetailById(theId);

		if (foundInstructorDetail == null) {
			// instructor detail not found
			log.info("### InstructorDetail with ID: {} not found !###", theId);
		} else {
			// print the instructor detail
			log.info("### The found InstructorDetail: {} ###", foundInstructorDetail);

			// print the associated instructor
			log.info("### The associated instructor: {} ###", foundInstructorDetail.getInstructor());
		}

		logEndMessage();
	}

	private void deleteInstructor(AppDAO appDAO) {

		int theId = 1;
        log.info("Deleting instructor id: {}", theId);

		appDAO.deleteInstructorById(theId);

		logEndMessage();
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
		Instructor instrToCreate = Instructor.builder()
				.firstName("Madhu")
				.lastName("Patel")
				.email("madhu@luv2code.com")
				.build();

		// create the instructor detail
		InstructorDetail instrDetailToCreate = InstructorDetail.builder()
				.youtubeChannel("http://www.luv2code.com/youtube")
				.hobby("Guitar").build();

		// associate the objects
		instrToCreate.setInstructorDetail(instrDetailToCreate);

		// save the instructor
		//
		// NOTE: this will ALSO save the instructor details object
		// because of CascadeType.ALL
		//
        log.info("Saving instructor: {}", instrToCreate);
		appDAO.save(instrToCreate);

		logEndMessage();
	}

	private static void logEndMessage() {
		log.info("Done!");
	}

	private static void logFindingInstrDetailMsg(int theId) {
		log.info("### Finding InstructorDetail with {}... ###", theId);
	}
}








