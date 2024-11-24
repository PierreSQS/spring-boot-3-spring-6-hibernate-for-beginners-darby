package com.luv2code.springboot.cruddemo.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/api/employees")
                        .with(httpBasic("John", "John")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName").value(equalTo("Leslie")))
                .andDo(print());
    }

    @Test
    void getEmployee() throws Exception {

        mockMvc.perform(get("/api/employees")
                        .with(httpBasic("John", "John")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName").value(equalTo("Leslie")))
                .andDo(print());
    }

    @Test
    void addEmployee() {
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void deleteEmployee() {
    }
}