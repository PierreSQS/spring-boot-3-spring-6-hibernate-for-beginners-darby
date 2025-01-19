package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.AppDAO;
import com.luv2code.cruddemo.entity.Instructor;
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

		return runner -> findInstructor(appDAO);
	}

	private void findInstructor(AppDAO appDAO) {

		int theId = 1;
        log.info("### Finding the instructor with the ID: {}... ###", theId);

		Instructor foundInstructor = appDAO.findInstructorById(theId);

        log.info("### Found Instructor: {} ###", foundInstructor);
        log.info("### The associated instructorDetail only: {} ###", foundInstructor.getInstructorDetail());

	}
	
}








