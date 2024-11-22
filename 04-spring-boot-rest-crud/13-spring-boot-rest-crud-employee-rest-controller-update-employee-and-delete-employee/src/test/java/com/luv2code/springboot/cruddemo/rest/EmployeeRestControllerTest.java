package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeRestController.class)
class EmployeeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService empServ;

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
        // given, when
        given(empServ.findAll()).willReturn(List.of(emp1,emp2));

        // then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(equalTo(2)))
                .andExpect(jsonPath("$.[0].lastName").value(equalTo("test1")))
                .andDo(print());
    }

    @Test
    void findEmployeeByID() throws Exception{
        // given
        emp1.setId(1); // id != 0 , simulates an Employee from the DB
        // when
        given(empServ.findById(anyInt())).willReturn(emp1);

        // then
        mockMvc.perform(get("/api/employees/{empID}",emp1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value(equalTo("test1")))
                .andDo(print());
    }

    @Test
    void findEmployeeByIDNotFound(){
        // given
        // emp1.id = 0, simulates a non-existing employee in the DB
        int empID = emp1.getId();
        String errMsg = "Employee with ID " + empID + " not found!";

        given(empServ.findById(anyInt())).willThrow(new RuntimeException(errMsg));

        // then assert that the request is throwing an Exception
        assertThatExceptionOfType(ServletException.class)
                .isThrownBy(() -> mockMvc.perform(get("/api/employees/{empID}", empID)))
                .havingCause()
                .withMessage(errMsg);

    }

    @Test
    void createEmployee() throws Exception {
        // given
        Employee createdEmp = emp2;
        createdEmp.setId(2);

        given(empServ.save(any())).willReturn(createdEmp);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(2)))
                .andExpect(jsonPath("$.firstName", equalTo("emp2")))
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
        emp2.setFirstName("empChanged");
        emp2.setLastName("testChange2");

        given(empServ.save(any())).willReturn(emp2);

        // when, then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empToUpdate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo("empChanged")))
                .andExpect(jsonPath("$.lastName", equalTo("testChange2")))
                .andDo(print());
    }
}