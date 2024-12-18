package com.luv2code.springboot.thymeleafdemo.controller;

import com.luv2code.springboot.thymeleafdemo.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
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
                .country("Brazil")
                .build();
    }

    @Test
    void showForm() throws Exception {
        mockMvc.perform(get("/showStudentForm"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", new Student()))
                .andExpect(model().attribute("countries", hasItems("Brazil","Cameroun")))
                .andExpect(view().name("student-form"))
                .andExpect(content().string(containsString("<h3>Student Registration Form</h3>")))
                .andExpect(content().string(containsString("<option value=\"Cameroun\" >Cameroun</option>")))
                .andDo(print());
    }

    @Test
    void processForm() throws Exception {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("firstName", student1.getFirstName());
        multiValueMap.add("lastName", student1.getLastName());
        multiValueMap.add("country", student1.getCountry());

        mockMvc.perform(post("/processStudentForm").params(multiValueMap)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student",student1))
                .andExpect(view().name("student-confirmation"))
                .andExpect(content()
                        .string(containsString("The student is confirmed: <span >Test User</span>")))
                .andExpect(content().string(containsString("Country: <span >Brazil</span>")))
                .andDo(print());
    }
}