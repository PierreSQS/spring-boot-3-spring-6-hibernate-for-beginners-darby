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

		return runner -> createInstructorWithCourses(appDAO);
	}

	private void createInstructorWithCourses(AppDAO appDAO) {

		// create the instructor
		Instructor instrToSave = Instructor.builder()
				.firstName("Susan")
				.lastName("Public")
				.email("susan.public@luv2code.com")
				.build();

		// create the instructor detail
		InstructorDetail instrDetailToSave = InstructorDetail.builder()
				.youtubeChannel("http://www.youtube.com")
				.hobby("Video Games").build();

		// associate the objects
		instrToSave.setInstructorDetail(instrDetailToSave);

		// create some courses
		Course tempCourse1 = Course.builder()
				.title("Air Guitar - The Ultimate Guide")
				.build();

		Course tempCourse2 = Course.builder()
				.title(" Pinball Masterclass")
				.build();

		// add courses to instructor
		instrToSave.add(tempCourse1);
		instrToSave.add(tempCourse2);

		// save the instructor
		//
		// NOTE: this will ALSO save the courses
		// because of CascadeType.PERSIST
		//
		saveInstructorMessage(instrToSave);
        log.info("### The courses to save: {} ###", instrToSave.getCourses());
		appDAO.save(instrToSave);

		doneMessage();

		log.info("### the saved Courses: {} ###", instrToSave.getCourses());
	}

	private static void doneMessage() {
		log.info("### Saving Instructor Done! ###");
	}

	private static void saveInstructorMessage(Instructor instructor) {
        log.info("### Saving instructor: {} ###", instructor);
	}
}








