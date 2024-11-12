package com.luv2code.cruddemo;

import com.luv2code.cruddemo.dao.AppDAO;
import com.luv2code.cruddemo.entity.Course;
import com.luv2code.cruddemo.entity.Review;
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
		//	createCoursesAndReviews(appDAO);
			deleteCourseAndReviewsByID(appDAO);
		};
	}

	private void deleteCourseAndReviewsByID(AppDAO appDAO) {
		int courseID = 10;

		log.info("Finding Course with Reviews by ID: {}", courseID);
		Course foundCourse = appDAO.findCourseAndReviewsByCourseId(courseID);
		log.info("the found course before deleting: {}",foundCourse);
		log.info("the reviews of the course before deleting: {}",foundCourse.getReviews());

		log.info("Deleting the course...");
		appDAO.deleteCourseByID(courseID);

		printDoneMessage();
	}

	private void createCoursesAndReviews(AppDAO appDAO) {
		// create course
		log.info("Creating Course...");
		Course course = Course.builder().title("Kubernetes for beginners").build();

		// create reviews
		log.info("Creating Reviews...");
		Review review1 = Review.builder().comment("What a nice course").build();
		Review review2 = Review.builder().comment("Cool! the best I have seen so far").build();
		Review review3 = Review.builder().comment("Not a beginner's tutorial").build();

		Set<Review> reviewSet = Set.of(review1, review2, review3);

		// link the reviews to course
		course.setReviews(reviewSet);

		// save the course and will also save the reviews
		log.info("The course before saving: {}", course);
		log.info("The reviews before saving: {}", course.getReviews());
		appDAO.saveCourse(course);
		log.info("The course after saving: {}", course);

		printDoneMessage();
	}

	private static void printDoneMessage() {
		log.info("Done!");
	}

}








