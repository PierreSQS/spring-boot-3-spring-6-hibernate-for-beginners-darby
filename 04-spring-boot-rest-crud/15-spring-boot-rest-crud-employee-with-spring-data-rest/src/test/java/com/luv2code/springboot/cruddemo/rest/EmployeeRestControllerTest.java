package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    Employee emp1, emp2;

    @BeforeEach
    void setUp() {
        emp1 = Employee.builder()
                .firstName("emp1")
                .lastName("test1")
                .email("emp1.test1@example.com")
                .build();

        emp2 = Employee.builder()
                .firstName("emp2")
                .lastName("test2")
                .email("emp2.test2@example.com")
                .build();

    }

    @Test
    void findAllEmployees() throws Exception {

        // when, then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(equalTo(3)))
                .andExpect(jsonPath("$._embedded.employees[0].lastName")
                        .value(equalTo("Andrews")))
                .andDo(print());
    }

    @Test
    void findEmployeeByID() throws Exception{

        // when, then
        mockMvc.perform(get("/api/employees/{empID}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value(equalTo("Andrews")))
                .andDo(print());
    }

    @Test
    void findEmployeeByIDNotFound() throws Exception {
        // given
        // emp1.id = 99, a non-existing employee in the DB
        int empID = 99;

        mockMvc.perform(get("/api/employees/{empID}", empID))
                .andExpect(status().isNotFound());

    }

    @Test
    void createEmployee() throws Exception {

        // when, then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp1)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void updateEmployee() throws Exception {
        // given
        Employee empToUpdate = Employee
                .builder()
                .id(2)
                .firstName("emp2")
                .lastName("test2")
                .build();

        emp2.setId(2);
        emp2.setFirstName("empChanged2");
        emp2.setLastName("testChange2");


        // when, then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empToUpdate)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("http://localhost/api/employees/2"))
                .andDo(print());
    }

    @Disabled("still doesn't work but works in standalone")
    @Test
    void deleteEmployeeByID() throws Exception {

        // when then
        // mockMvc.perform(delete("/employee/{empID}",1))
        mockMvc.perform(delete("/api/employees/6"))
                .andExpect(status().isNoContent())
                .andDo(print());

    }
}