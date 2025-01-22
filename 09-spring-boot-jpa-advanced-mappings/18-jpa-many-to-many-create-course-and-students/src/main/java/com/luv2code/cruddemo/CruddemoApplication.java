package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.AppDAO;
import com.luv2code.cruddemo.entity.Course;
import com.luv2code.cruddemo.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@Slf4j
@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AppDAO appDAO) {

		return runner -> createCoursesAndStudents(appDAO);
	}

	private void createCoursesAndStudents(AppDAO appDAO) {
		// create Course
		log.info("### Creating courses... ###");
		Course course = Course.builder().title("Security in Payments Systems").build();
		log.info("created course {}", course);

		// create Students
		log.info("Creating students...");
		Student student1 = Student.builder()
				.firstName("Pierrot")
				.lastName("Mongonnam")
				.email("pierrot.mongonnam@gmail.com")
				.build();

		Student student2 = Student.builder()
				.firstName("Odile")
				.lastName("Mongonnam")
				.email("odile.mongonnam@gmail.com")
				.build();
		log.info("### Creating courses... ###");


		log.info("### Created Set of Students... ###");
		Set<Student> studentSet = Set.of(student1, student2);
		log.info("### Created Set of {} students ###", studentSet.size());


		// Link Students to Course
		log.info("### Linking students to course... ###");
		course.addStudents(studentSet);

		// save Course
		log.info("### Saving course... ###");
		appDAO.saveCourse(course);

		log.info("### Saved the Course {} ###", course);
		log.info("### The associated students in the course {} ###", course.getStudents());


		printDoneMessage();
	}

	private static void printDoneMessage() {
		log.info("### Done! ###");
	}

}








