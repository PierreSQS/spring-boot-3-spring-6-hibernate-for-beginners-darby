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

		return runner -> {

			addMoreCoursesForStudent(appDAO);

		};
	}

	private void addMoreCoursesForStudent(AppDAO appDAO) {
		int studentID = 1;

		// finding the first student in the Student Set
		log.info("Finding the first student in the Student Set...");
		Student foundStudent = appDAO.findStudentAndCoursesByStudentID(studentID);

		log.info("Found Student: {}", foundStudent);

		// list the Students to Course
		log.info("Listing the courses of student with the ID: {}...", studentID);
		log.info("The courses of the student before updating: {}", foundStudent.getCourses());

		// Creating New Courses
		log.info("Creating 2 new courses....");
		Course course1 = Course.builder().title("New Course1").build();
		Course course2 = Course.builder().title("New Course2").build();

		// adding courses to student
		log.info("Adding courses to student...");
		foundStudent.addCourses(Set.of(course1, course2));

		// updating student
		appDAO.updateStudent(foundStudent);

		log.info("the courses of the student after updating: {}",foundStudent);

		printDoneMessage();
	}

	private static void printDoneMessage() {
		log.info("Done!");
	}

}








