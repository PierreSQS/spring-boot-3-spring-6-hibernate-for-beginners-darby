package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
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

@WebMvcTest(EmployeeRestController.class)
public class EmployeeRestControllerEdgeCaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee1;

    @BeforeEach
    void setUp() {
        // Set up test data
        employee1 = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();
    }

    @Test
    @DisplayName("POST /api/employees - With Null Fields")
    void testAddEmployeeWithNullFields() throws Exception {
        // Given
        Employee employeeWithNullFields = Employee.builder()
                .firstName(null)
                .lastName(null)
                .email(null)
                .build();

        when(employeeService.save(any(Employee.class))).thenReturn(employeeWithNullFields);

        // When & Then
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeWithNullFields)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").doesNotExist())
                .andExpect(jsonPath("$.lastName").doesNotExist())
                .andExpect(jsonPath("$.email").doesNotExist());

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("POST /api/employees - With Empty Fields")
    void testAddEmployeeWithEmptyFields() throws Exception {
        // Given
        Employee employeeWithEmptyFields = Employee.builder()
                .firstName("")
                .lastName("")
                .email("")
                .build();

        when(employeeService.save(any(Employee.class))).thenReturn(employeeWithEmptyFields);

        // When & Then
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeWithEmptyFields)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("")))
                .andExpect(jsonPath("$.lastName", is("")))
                .andExpect(jsonPath("$.email", is("")));

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("POST /api/employees - With Very Long Fields")
    void testAddEmployeeWithVeryLongFields() throws Exception {
        // Given
        String veryLongString = "a".repeat(1000);
        Employee employeeWithLongFields = Employee.builder()
                .firstName(veryLongString)
                .lastName(veryLongString)
                .email(veryLongString + "@example.com")
                .build();

        when(employeeService.save(any(Employee.class))).thenReturn(employeeWithLongFields);

        // When & Then
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeWithLongFields)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(veryLongString)))
                .andExpect(jsonPath("$.lastName", is(veryLongString)))
                .andExpect(jsonPath("$.email", is(veryLongString + "@example.com")));

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("POST /api/employees - With Database Constraint Violation")
    void testAddEmployeeWithDatabaseConstraintViolation() throws Exception {
        // Given
        Employee newEmployee = Employee.builder()
                .firstName("New")
                .lastName("Employee")
                .email("duplicate@example.com")
                .build();

        when(employeeService.save(any(Employee.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate email address"));

        // When & Then
        try {
            mockMvc.perform(post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newEmployee)));
        } catch (Exception e) {
            // Expected exception
            assert e.getCause() instanceof DataIntegrityViolationException;
            assert e.getCause().getMessage().contains("Duplicate email address");
        }

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("PATCH /api/employees/{id} - With Empty Request Body")
    void testPatchEmployeeWithEmptyBody() throws Exception {
        // When & Then
        mockMvc.perform(patch("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(result -> {
                    // Verify that the exception was thrown
                    Throwable exception = result.getResolvedException();
                    assert exception != null;
                    assert exception instanceof org.springframework.http.converter.HttpMessageNotReadableException;
                });

        verify(employeeService, never()).findById(anyInt());
        verify(employeeService, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("PATCH /api/employees/{id} - With Multiple Fields")
    void testPatchEmployeeWithMultipleFields() throws Exception {
        // Given
        Map<String, Object> patchPayload = new HashMap<>();
        patchPayload.put("firstName", "Patched");
        patchPayload.put("lastName", "Name");
        patchPayload.put("email", "patched@example.com");

        Employee existingEmployee = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        Employee patchedEmployee = Employee.builder()
                .id(1)
                .firstName("Patched")
                .lastName("Name")
                .email("patched@example.com")
                .build();

        when(employeeService.findById(1)).thenReturn(existingEmployee);
        when(employeeService.save(any(Employee.class))).thenReturn(patchedEmployee);

        // When & Then
        mockMvc.perform(patch("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Patched")))
                .andExpect(jsonPath("$.lastName", is("Name")))
                .andExpect(jsonPath("$.email", is("patched@example.com")));

        verify(employeeService, times(1)).findById(1);
        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} - With Non-Existent ID")
    void testDeleteEmployeeWithNonExistentId() throws Exception {
        // Given
        when(employeeService.findById(999)).thenThrow(new RuntimeException("Did not find employee id - 999"));

        // When & Then
        try {
            mockMvc.perform(delete("/api/employees/999"));
        } catch (Exception e) {
            // Expected exception
            assert e.getCause() instanceof RuntimeException;
            assert e.getCause().getMessage().contains("Did not find employee id - 999");
        }

        verify(employeeService, times(1)).findById(999);
        verify(employeeService, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("GET /api/employees/{id} - With Invalid ID Format")
    void testGetEmployeeWithInvalidIdFormat() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/employees/abc"))
                .andExpect(result -> {
                    // Verify that the exception was thrown
                    Throwable exception = result.getResolvedException();
                    assert exception != null;
                    assert exception instanceof org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
                });

        verify(employeeService, never()).findById(anyInt());
    }

    @Test
    @DisplayName("PUT /api/employees - With Missing ID")
    void testUpdateEmployeeWithMissingId() throws Exception {
        // Given
        Employee employeeWithoutId = Employee.builder()
                .firstName("Updated")
                .lastName("Employee")
                .email("updated@example.com")
                .build();

        Employee savedEmployee = Employee.builder()
                .id(1)
                .firstName("Updated")
                .lastName("Employee")
                .email("updated@example.com")
                .build();

        when(employeeService.save(any(Employee.class))).thenReturn(savedEmployee);

        // When & Then
        mockMvc.perform(put("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeWithoutId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Updated")))
                .andExpect(jsonPath("$.lastName", is("Employee")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));

        verify(employeeService, times(1)).save(any(Employee.class));
    }
}