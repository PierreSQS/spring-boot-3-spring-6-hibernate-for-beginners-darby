package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.StudentDAO;
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
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO) {

		return runner -> deleteAllStudents(studentDAO);
	}

	private void deleteAllStudents(StudentDAO studentDAO) {

		log.info("Deleting all students...");
		int numRowsDeleted = studentDAO.deleteAll();
        log.info("Deleted row count: {}", numRowsDeleted);
		log.info("***** Done! *****");
	}


}







