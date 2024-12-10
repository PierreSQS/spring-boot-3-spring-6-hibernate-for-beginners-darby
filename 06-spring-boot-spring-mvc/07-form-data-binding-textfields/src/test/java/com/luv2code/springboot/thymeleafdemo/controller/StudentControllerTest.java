package com.luv2code.springboot.thymeleafdemo.controller;

import com.luv2code.springboot.thymeleafdemo.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    Student student1;

    @BeforeEach
    void setUp() {
        student1 = Student.builder()
                .firstName("Test")
                .lastName("User")
                .build();
    }

    @Test
    void showForm() throws Exception {
        mockMvc.perform(get("/showStudentForm"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", new Student()))
                .andExpect(view().name("student-form"))
                .andExpect(content().string(containsString("<h3>Student Registration Form</h3>")))
                .andDo(print());
    }

    @Test
    void processForm() throws Exception {

        mockMvc.perform(post("/processStudentForm")
                        .content(student1.toString()))
                .andExpect(status().isOk())
                .andDo(print());
    }
}