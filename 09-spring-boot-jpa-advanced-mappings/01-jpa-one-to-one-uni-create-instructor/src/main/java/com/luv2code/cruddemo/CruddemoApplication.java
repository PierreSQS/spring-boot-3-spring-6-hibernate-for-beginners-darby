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

		return runner -> createInstructor(appDAO);
	}

	private void createInstructor(AppDAO appDAO) {

		log.info("creating the instructor and the instructor's details...");
		Instructor createdInstructor = Instructor.builder()
				.firstName("Madhu")
				.lastName("Patel")
				.email("madhu@luv2code.com").build();

		// create the instructor detail
		InstructorDetail createInstrDetail = InstructorDetail.builder()
				.youtubeChannel("http://www.luv2code.com/youtube")
				.hobby("Guitar")
				.build();

		// associate the objects
		createdInstructor.setInstructorDetail(createInstrDetail);

		// save the instructor
		//
		// NOTE: this will ALSO save the Instructor Details object
		// because of CascadeType.ALL
		//
        log.info("### Saving instructor: {} ... ###", createdInstructor); // instructor doesn't have an ID
		appDAO.save(createdInstructor);

		log.info("### The saved Instructor: {} ###", createdInstructor); // instructor new has an ID

		log.info("### Done! ###");
	}
}








