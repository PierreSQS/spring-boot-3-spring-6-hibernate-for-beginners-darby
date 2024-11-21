package com.luv2code.demo.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {StudentRestController.class})
class StudentRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getStudents() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",equalTo(3)))
                .andExpect(jsonPath("$.[0].firstName",equalTo("Poornima")))
                .andExpect(jsonPath("$").isArray())
                .andDo(print());
    }

    @Test
    void getStudent() throws Exception {
        int studentID= 0;
        mockMvc.perform(get("/api/students/{studentID}",studentID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",equalTo("Poornima")))
                .andDo(print());
    }

    @Test
    void getStudentNotFound() throws Exception {
        int studentID= 99;
        mockMvc.perform(get("/api/students/{studentID}",studentID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message",
                        equalTo("Student with " + studentID + " not found !")))
                .andDo(print());
    }

    @Test
    void getStudentBadRequest() throws Exception {
        String studentID= "abcd";
        mockMvc.perform(get("/api/students/{studentID}",studentID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andDo(print());
    }
}