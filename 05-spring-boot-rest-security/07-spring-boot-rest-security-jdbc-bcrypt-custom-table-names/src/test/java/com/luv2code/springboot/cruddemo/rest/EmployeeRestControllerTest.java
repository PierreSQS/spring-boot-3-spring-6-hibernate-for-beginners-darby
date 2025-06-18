package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
// Note: @MockBean is deprecated since version 3.4.0 and marked for removal
// The issue description mentions using @MockitoBean, but this annotation is not available in the current version
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private List<Employee> employees;
    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        // Create test data
        employee1 = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        employee2 = Employee.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .build();

        employees = Arrays.asList(employee1, employee2);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void findAll_ShouldReturnAllEmployees() throws Exception {
        // Given
        when(employeeService.findAll()).thenReturn(employees);

        // When & Then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));

        verify(employeeService).findAll();
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getEmployee_ShouldReturnEmployee() throws Exception {
        // Given
        when(employeeService.findById(1)).thenReturn(employee1);

        // When & Then
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        verify(employeeService).findById(1);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addEmployee_ShouldCreateNewEmployee() throws Exception {
        // Given
        Employee newEmployee = Employee.builder()
                .firstName("New")
                .lastName("Employee")
                .email("new@example.com")
                .build();

        Employee savedEmployee = Employee.builder()
                .id(3)
                .firstName("New")
                .lastName("Employee")
                .email("new@example.com")
                .build();

        when(employeeService.save(any(Employee.class))).thenReturn(savedEmployee);

        // When & Then
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.firstName", is("New")))
                .andExpect(jsonPath("$.lastName", is("Employee")))
                .andExpect(jsonPath("$.email", is("new@example.com")));

        verify(employeeService).save(any(Employee.class));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void updateEmployee_ShouldUpdateExistingEmployee() throws Exception {
        // Given
        Employee updatedEmployee = Employee.builder()
                .id(1)
                .firstName("Updated")
                .lastName("Employee")
                .email("updated@example.com")
                .build();

        when(employeeService.save(any(Employee.class))).thenReturn(updatedEmployee);

        // When & Then
        mockMvc.perform(put("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Updated")))
                .andExpect(jsonPath("$.lastName", is("Employee")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));

        verify(employeeService).save(any(Employee.class));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void patchEmployee_ShouldPartiallyUpdateEmployee() throws Exception {
        // Given
        Map<String, Object> patchPayload = new HashMap<>();
        patchPayload.put("firstName", "Patched");

        when(employeeService.findById(1)).thenReturn(employee1);
        when(employeeService.save(any(Employee.class))).thenReturn(
                Employee.builder()
                        .id(1)
                        .firstName("Patched")
                        .lastName("Doe")
                        .email("john@example.com")
                        .build()
        );

        // When & Then
        mockMvc.perform(patch("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Patched")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        verify(employeeService).findById(1);
        verify(employeeService).save(any(Employee.class));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void patchEmployee_WithIdInPayload_ShouldThrowException() throws Exception {
        // Given
        Map<String, Object> patchPayload = new HashMap<>();
        patchPayload.put("id", 999);
        patchPayload.put("firstName", "Patched");

        when(employeeService.findById(1)).thenReturn(employee1);

        // When & Then
        // With @SpringBootTest, we expect the controller to throw an exception
        // which will be wrapped in a ServletException
        try {
            mockMvc.perform(patch("/api/employees/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(patchPayload)));
            // If we get here, the test should fail because no exception was thrown
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            // Verify that the exception is a ServletException with a RuntimeException as the cause
            assertTrue(e.getCause() instanceof RuntimeException);
            assertTrue(e.getCause().getMessage().contains("Employee id not allowed in request body"));
        }

        verify(employeeService).findById(1);
        verify(employeeService, never()).save(any(Employee.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteEmployee_ShouldRemoveEmployee() throws Exception {
        // Given
        when(employeeService.findById(1)).thenReturn(employee1);
        doNothing().when(employeeService).deleteById(1);

        // When & Then
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Deleted employee id - 1")));

        verify(employeeService).findById(1);
        verify(employeeService).deleteById(1);
    }
}
