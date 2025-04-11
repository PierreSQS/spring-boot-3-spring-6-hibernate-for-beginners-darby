package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeRestController.class)
class EmployeeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("GET /employees returns list of employees")
    void getEmployeesReturnsList() throws Exception {
        when(employeeService.findAll()).thenReturn(List.of(employee));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    @DisplayName("GET /employees returns empty list when no employees exist")
    void getEmployeesReturnsEmptyList() throws Exception {
        when(employeeService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("GET /employees/{id} returns employee when found")
    void getEmployeeByIdReturnsEmployee() throws Exception {
        when(employeeService.findById(1)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("GET /employees/{id} returns 404 when employee not found")
    void getEmployeeByIdReturnsNotFound() throws Exception {
        when(employeeService.findById(1)).thenReturn(null);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /employees creates a new employee")
    void postEmployeeCreatesNewEmployee() throws Exception {
        Employee savedEmployee = new Employee();
        savedEmployee.setId(2);
        savedEmployee.setFirstName("Jane");

        when(employeeService.save(any(Employee.class))).thenReturn(savedEmployee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    @DisplayName("PUT /employees updates an existing employee")
    void putEmployeeUpdatesEmployee() throws Exception {
        when(employeeService.save(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(put("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("PATCH /employees/{id} partially updates an employee")
    void patchEmployeeUpdatesEmployee() throws Exception {
        Map<String, Object> patchPayload = Map.of("lastName", "Smith");
        Employee patchedEmployee = new Employee();
        patchedEmployee.setId(1);
        patchedEmployee.setFirstName("John");
        patchedEmployee.setLastName("Smith");

        when(employeeService.findById(1)).thenReturn(employee);
        when(objectMapper.convertValue(any(), eq(Employee.class))).thenReturn(patchedEmployee);
        when(employeeService.save(any(Employee.class))).thenReturn(patchedEmployee);

        mockMvc.perform(patch("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patchPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    @DisplayName("DELETE /employees/{id} deletes an employee")
    void deleteEmployeeDeletesEmployee() throws Exception {
        when(employeeService.findById(1)).thenReturn(employee);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted employee id - 1"));
    }

    @Test
    @DisplayName("DELETE /employees/{id} returns 404 when employee not found")
    void deleteEmployeeReturnsNotFound() throws Exception {
        when(employeeService.findById(1)).thenReturn(null);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNotFound());
    }
}