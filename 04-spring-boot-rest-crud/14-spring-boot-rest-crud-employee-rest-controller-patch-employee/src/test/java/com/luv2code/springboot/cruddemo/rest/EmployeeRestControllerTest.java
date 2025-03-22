package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    EmployeeService employeeService;

    Employee empMock;

    @BeforeEach
    void setUp() {
        empMock = Employee.builder()
                .firstName("Test")
                .lastName("User")
                .email("testuser@exampel.com")
                .build();
    }

    @Test
    void findAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        given(employeeService.findAll()).willReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andDo(print());
    }

    @Test
    void getEmployeeById() throws Exception {
        empMock.setId(1);
        given(employeeService.findById(anyInt())).willReturn(empMock);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andDo(print());
    }

    @Test
    void getEmployeeByIdNotFound() {
        given(employeeService.findById(1)).willReturn(null);

        assertThatThrownBy(() -> mockMvc.perform(get("/api/employees/1")))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("Employee id not found - 1");
    }

    @Test
    void addNewEmployee() throws Exception {
        Employee empToSave = Employee.builder()
                .firstName(empMock.getFirstName())
                .lastName(empMock.getLastName())
                .email(empMock.getEmail())
                .build();

        empMock.setId(1);
        given(employeeService.save(any(Employee.class))).willReturn(empMock);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empToSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andDo(print());
    }

    @Test
    void updateExistingEmployee() throws Exception {
        Employee employeeToUpdate = Employee.builder()
                .id(1)
                .firstName("Test")
                .lastName("to update")
                .email("toupdate@example.com")
                .build();

        empMock.setId(1);

        given(employeeService.save(any(Employee.class))).willReturn(empMock);

        mockMvc.perform(put("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andDo(print());
    }
}