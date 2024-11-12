package com.luv2code.cruddemo.dao;

import com.luv2code.cruddemo.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AppDAOImplTest {

    @Autowired
    AppDAO appDAO;


    @Test
    void deleteCourseById() {

        Course course = appDAO.getAllCourses().getFirst();

        int courseId = course.getId();
        assertThat(courseId).isNotNull();

        appDAO.deleteCourseByID(courseId);

        Course courseByID = appDAO.findCourseByID(course.getId());

        assertThat(courseByID).isNull();

    }
}