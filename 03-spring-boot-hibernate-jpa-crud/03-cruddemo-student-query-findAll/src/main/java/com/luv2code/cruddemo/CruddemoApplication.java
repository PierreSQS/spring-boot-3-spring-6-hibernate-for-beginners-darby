package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.StudentDAO;
import com.luv2code.cruddemo.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Slf4j
@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO) {

		return runner -> findAllEmployees(studentDAO);

}

	private void findAllEmployees(StudentDAO studentDAO) {
		log.info("Finding all employees...");
		List<Student> allEmployees = studentDAO.findAllEmployees();

		log.info("The employees {}", allEmployees);

		doneMessage();

	}

	private static void doneMessage() {
		log.info("****** Done! ******");
	}

}







