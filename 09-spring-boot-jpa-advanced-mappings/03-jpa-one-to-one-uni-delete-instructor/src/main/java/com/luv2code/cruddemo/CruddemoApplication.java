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

			deleteInstructor(appDAO);
		};
	}

	private void deleteInstructor(AppDAO appDAO) {

		int theId = 2;
        log.info("### Deleting instructor id: {} ###", theId);

		// delete the instructor
		//
		// NOTE: this will ALSO delete the instructor details object
		// because of CascadeType.ALL
		//
		appDAO.deleteInstructorById(theId);

		doneMessage();
	}

	private void createInstructor(AppDAO appDAO) {

		// create the instructor
		Instructor instrToCreate = Instructor.builder()
				.firstName("Madhu")
				.lastName("Patel")
				.email("madhu@luv2code.com")
				.build();

		// create the instructor detail
		InstructorDetail instrDetails = InstructorDetail.builder()
				.youtubeChannel("http://www.luv2code.com/youtube")
				.hobby("Guitar").build();

		// associate the objects
		instrToCreate.setInstructorDetail(instrDetails);

		// save the instructor
		//
		// NOTE: this will ALSO save the instructor details object
		// because of CascadeType.ALL
		//
        log.info("### Saving instructor: {} ###", instrToCreate);
		appDAO.save(instrToCreate);

		log.info("### the saved instructor {} ###", instrToCreate);

		doneMessage();
	}

	private static void doneMessage() {
		log.info("Done!");
	}
}








