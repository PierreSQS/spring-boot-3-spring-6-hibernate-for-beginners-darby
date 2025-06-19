package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeRestController.class)
public class EmployeeRestControllerEdgeCaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Set up test data
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
    void testAddEmployeeWithDatabaseConstraintViolation() {
        // Given
        Employee newEmployee = Employee.builder()
                .firstName("New")
                .lastName("Employee")
                .email("duplicate@example.com")
                .build();

        when(employeeService.save(any(Employee.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate email address"));

        // When & Then
        assertThatThrownBy(() -> mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee))))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("Duplicate email address");

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("PATCH /api/employees/{id} - With Empty Request Body")
    void testPatchEmployeeWithEmptyBody() throws Exception {
        // When & Then
        mockMvc.perform(patch("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
        .andExpect(status().isBadRequest())
        .andDo(print());

        // Verify that no service methods were called
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
    void testDeleteEmployeeWithNonExistentId() {
        // Given
        when(employeeService.findById(999)).thenThrow(new RuntimeException("Did not find employee id - 999"));

        // When & Then
        assertThatThrownBy(() -> mockMvc.perform(delete("/api/employees/999")))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("Did not find employee id - 999");

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