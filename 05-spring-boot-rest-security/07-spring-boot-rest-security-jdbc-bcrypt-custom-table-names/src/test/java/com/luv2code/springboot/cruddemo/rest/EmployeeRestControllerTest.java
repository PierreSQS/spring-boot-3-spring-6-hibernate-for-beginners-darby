package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeRestControllerTest {

    @ControllerAdvice
    static class RestExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Mock(lenient = true)
    private EmployeeService employeeService;

    @Mock(lenient = true)
    private ObjectMapper objectMapper;

    @InjectMocks
    private EmployeeRestController employeeRestController;

    private MockMvc mockMvc;

    // Real ObjectMapper for test serialization
    private ObjectMapper realObjectMapper = new ObjectMapper();

    private List<Employee> employees;
    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        // Set up MockMvc with exception handling
        mockMvc = MockMvcBuilders.standaloneSetup(employeeRestController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

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

        // Configure ObjectMapper mock for the patch method
        when(objectMapper.convertValue(any(Employee.class), eq(ObjectNode.class)))
                .thenAnswer(invocation -> {
                    Employee emp = invocation.getArgument(0);
                    ObjectNode node = mock(ObjectNode.class);
                    return node;
                });

        when(objectMapper.convertValue(any(Map.class), eq(ObjectNode.class)))
                .thenAnswer(invocation -> {
                    Map<String, Object> map = invocation.getArgument(0);
                    ObjectNode node = mock(ObjectNode.class);
                    return node;
                });

        when(objectMapper.convertValue(any(ObjectNode.class), eq(Employee.class)))
                .thenAnswer(invocation -> employee1);
    }

    @Test
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
                .content(realObjectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.firstName", is("New")))
                .andExpect(jsonPath("$.lastName", is("Employee")))
                .andExpect(jsonPath("$.email", is("new@example.com")));

        verify(employeeService).save(any(Employee.class));
    }

    @Test
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
                .content(realObjectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Updated")))
                .andExpect(jsonPath("$.lastName", is("Employee")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));

        verify(employeeService).save(any(Employee.class));
    }

    @Test
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
                .content(realObjectMapper.writeValueAsString(patchPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Patched")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        verify(employeeService).findById(1);
        verify(employeeService).save(any(Employee.class));
    }

    @Test
    void patchEmployee_WithIdInPayload_ShouldThrowException() throws Exception {
        // Given
        Map<String, Object> patchPayload = new HashMap<>();
        patchPayload.put("id", 999);
        patchPayload.put("firstName", "Patched");

        when(employeeService.findById(1)).thenReturn(employee1);

        // When & Then
        mockMvc.perform(patch("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(realObjectMapper.writeValueAsString(patchPayload)))
                .andExpect(status().isInternalServerError());

        verify(employeeService).findById(1);
        verify(employeeService, never()).save(any(Employee.class));
    }

    @Test
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
