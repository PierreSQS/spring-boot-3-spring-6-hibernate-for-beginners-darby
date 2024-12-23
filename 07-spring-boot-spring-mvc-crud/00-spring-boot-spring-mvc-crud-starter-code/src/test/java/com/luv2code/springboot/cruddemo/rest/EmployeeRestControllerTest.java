package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeRestController.class)
class EmployeeRestControllerTest {

    @MockitoBean
    EmployeeService empServ;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    // Mocks
    Employee emp1, emp2;

    @BeforeEach
    void setUp() {
        emp1 = Employee.builder()
                .id(1)
                .firstName("Test1")
                .lastName("User1")
                .build();

        emp2 = Employee.builder()
                .id(2)
                .firstName("Test2")
                .lastName("User2")
                .build();

    }

    @Test
    void findAll() throws Exception {
        // Given
        given(empServ.findAll()).willReturn(List.of(emp1,emp2));

        // When, Then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(2)))
                .andDo(print());
    }

    @Test
    void getEmployee() throws Exception {
        // Given
        given(empServ.findById(anyInt())).willReturn(emp1);

        // When, Then
        mockMvc.perform(get("/api/employees/{empID}",emp1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Test1")))
                .andDo(print());
    }

    @Test
    void addEmployee() throws Exception {
        // Given
        Employee empToSave = Employee.builder()
                .firstName("User")
                .lastName("Saved")
                .build();

        Employee savedEmp = Employee.builder()
                .id(3)
                .firstName(empToSave.getFirstName())
                .lastName(empToSave.getLastName())
                .build();

        given(empServ.save(any())).willReturn(savedEmp);

        // When, Then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empToSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("User")))
                .andExpect(jsonPath("$.lastName", equalTo("Saved")))
                .andDo(print());
    }

    @Test
    void updateEmployee() throws Exception {
        // Given
        Employee updatedEmp = Employee.builder()
                .id(emp1.getId())
                .firstName(emp1.getFirstName())
                .lastName("Updated1")
                .build();

        given(empServ.save(any())).willReturn(updatedEmp);

        // When, Then
        mockMvc.perform(put("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(updatedEmp.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(updatedEmp.getLastName())))
                .andDo(print());
    }

    @Test
    void deleteEmployee() {
    }
}