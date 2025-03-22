package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeRestControllerAiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeServiceImpl empServ;

    @Autowired
    ObjectMapper objectMapper;

    Employee emp1, emp2;

    @BeforeEach
    void setUp() {
        emp1 = Employee.builder()
                .firstName("Hector")
                .lastName("Perez")
                .email("hector.perez@example.com")
                .build();

        emp2 = Employee.builder()
                .id(6)
                .firstName("Hector")
                .lastName("Fernandez")
                .email("hector.fernandez@example.com")
                .build();
    }

    @Test
    void findAllEmployees() throws Exception {
        mockMvc.perform(get("/api/employees")
                        .with(httpBasic("John", "John")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName").value(equalTo("Leslie")))
                .andDo(print());
    }

    @Test
    void getEmployeeById() throws Exception {
        Employee firstFoundEmp = empServ.findAll().getFirst();

        mockMvc.perform(get("/api/employees/{employeeId}", firstFoundEmp.getId())
                        .with(httpBasic("John", "John")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(equalTo("Leslie")))
                .andDo(print());
    }

    @Test
    void addNewEmployee() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .with(httpBasic("Mary", "Mary"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp1)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateExistingEmployee() throws Exception {
        mockMvc.perform(put("/api/employees")
                        .with(httpBasic("Susan", "Susan"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp2)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteEmployeeById() throws Exception {
        mockMvc.perform(delete("/api/employees/{employeeId}", 6)
                        .with(httpBasic("Susan", "Susan"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getEmployeeNotFound() throws Exception {
        mockMvc.perform(get("/api/employees/{employeeId}", 999)
                        .with(httpBasic("John", "John")))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void addEmployeeWithExistingId() throws Exception {
        emp1.setId(1);
        mockMvc.perform(post("/api/employees")
                        .with(httpBasic("Mary", "Mary"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp1)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void updateNonExistingEmployee() throws Exception {
        emp2.setId(999);
        mockMvc.perform(put("/api/employees")
                        .with(httpBasic("Susan", "Susan"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp2)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void deleteNonExistingEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees/{employeeId}", 999)
                        .with(httpBasic("Susan", "Susan"))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}