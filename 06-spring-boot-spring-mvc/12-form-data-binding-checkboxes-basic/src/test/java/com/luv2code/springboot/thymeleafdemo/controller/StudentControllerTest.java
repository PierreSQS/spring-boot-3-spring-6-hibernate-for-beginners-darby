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
                .favoriteLanguage("Java")
                .build();
    }

    @Test
    void showForm() throws Exception {
        mockMvc.perform(get("/showStudentForm"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", new Student()))
                .andExpect(model().attribute("countries", hasItems("Brazil","Cameroun")))
                .andExpect(model().attribute("languages", hasItems("Java","Perl")))
                .andExpect(view().name("student-form"))
                // show Student Registration Page
                .andExpect(content().string(containsString("<h3>Student Registration Form</h3>")))
                // show one Dropdown List Item
                .andExpect(content().string(containsString("<option value=\"Cameroun\" >Cameroun</option>")))
                // show one Radio Button
                .andExpect(content()
                        .string(containsString("<input type=\"radio\" value=\"Java\" " +
                                "id=\"favoriteLanguage1\" name=\"favoriteLanguage\" >Java</input>")))
                // show one Check Box
                .andExpect(content()
                        .string(containsString("<input type=\"checkbox\" value=\"Linux\" " +
                                "id=\"favoriteSystems1\" name=\"favoriteSystems\"><input type=\"hidden\"" +
                                " name=\"_favoriteSystems\" value=\"on\"/>Linux</input>\n")))
                .andDo(print());
    }

    @Test
    void processForm() throws Exception {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("firstName", student1.getFirstName());
        multiValueMap.add("lastName", student1.getLastName());
        multiValueMap.add("country", student1.getCountry());
        multiValueMap.add("favoriteLanguage", student1.getFavoriteLanguage());

        mockMvc.perform(post("/processStudentForm").params(multiValueMap)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student",student1))
                .andExpect(view().name("student-confirmation"))
                .andExpect(content()
                        .string(containsString("The student is confirmed: <span >Test User</span>")))
                .andExpect(content().string(containsString("Country: <span >Brazil</span>")))
                .andExpect(content().string(containsString("Favorite Programming Language: <span >Java</span>")))
                .andDo(print());
    }
}