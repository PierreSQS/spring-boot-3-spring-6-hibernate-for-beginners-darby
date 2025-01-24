package com.luv2code.cruddemo.dao;

import com.luv2code.cruddemo.entity.Instructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AppDAOImplTest {

    @Autowired
    AppDAO appDAO;


    @Transactional
    @Test
    void save() {
        Instructor instToSave = Instructor.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@luv2code.com")
                .build();
        System.out.println("#### sa Instructor before saving: "+instToSave);
        appDAO.save(instToSave);
        System.out.println("#### sa Instructor after saving: "+instToSave);

        assertThat(instToSave.getId()).isNotZero();
    }

    @Transactional
    @Test
    void findInstructorById() {
        Instructor instToSave = Instructor.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@luv2code.com")
                .build();
        System.out.println("#### fi Instructor before saving: "+instToSave);
        appDAO.save(instToSave);
        System.out.println("#### fi Instructor after saving: "+instToSave);

        Instructor instructorById = appDAO.findInstructorById(instToSave.getId());
        assertThat(instructorById.getId()).isEqualTo(instToSave.getId());
    }
}