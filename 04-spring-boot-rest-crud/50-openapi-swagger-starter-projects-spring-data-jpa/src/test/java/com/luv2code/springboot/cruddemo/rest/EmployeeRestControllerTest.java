package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

@ExtendWith(MockitoExtension.class)
public class EmployeeRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ObjectMapper objectMapperMock;

    @InjectMocks
    private EmployeeRestController employeeRestController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Employee employee1;
    private Employee employee2;
    private List<Employee> employees;

    @BeforeEach
    void setUp() {
        // Set up MockMvc
        mockMvc = MockMvcBuilders
                .standaloneSetup(employeeRestController)
                .build();

        // Set up test data
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

        // Configure objectMapperMock for the apply method in the controller - use lenient mode
        Mockito.lenient().when(objectMapperMock.convertValue(any(), eq(com.fasterxml.jackson.databind.node.ObjectNode.class)))
            .thenAnswer(invocation -> objectMapper.convertValue(invocation.getArgument(0), 
                com.fasterxml.jackson.databind.node.ObjectNode.class));

        Mockito.lenient().when(objectMapperMock.convertValue(any(), eq(Employee.class)))
            .thenAnswer(invocation -> objectMapper.convertValue(invocation.getArgument(0), Employee.class));
    }

    @Test
    @DisplayName("GET /api/employees - Success")
    void testFindAll() throws Exception {
        // Given
        when(employeeService.findAll()).thenReturn(employees);

        // When & Then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));

        verify(employeeService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/employees - Empty List")
    void testFindAllEmpty() throws Exception {
        // Given
        when(employeeService.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(employeeService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/employees/{id} - Success")
    void testGetEmployee() throws Exception {
        // Given
        when(employeeService.findById(1)).thenReturn(employee1);

        // When & Then
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        verify(employeeService, times(1)).findById(1);
    }

    @Test
    @DisplayName("GET /api/employees/{id} - Not Found")
    void testGetEmployeeNotFound() throws Exception {
        // Given
        when(employeeService.findById(999)).thenThrow(new RuntimeException("Did not find employee id - 999"));

        // When & Then
        try {
            mockMvc.perform(get("/api/employees/999"));
        } catch (Exception e) {
            // Expected exception
            assert e.getCause() instanceof RuntimeException;
            assert e.getCause().getMessage().contains("Did not find employee id - 999");
        }

        verify(employeeService, times(1)).findById(999);
    }

    @Test
    @DisplayName("POST /api/employees - Success")
    void testAddEmployee() throws Exception {
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

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("POST /api/employees - With ID")
    void testAddEmployeeWithId() throws Exception {
        // Given
        Employee employeeWithId = Employee.builder()
                .id(100)
                .firstName("With")
                .lastName("ID")
                .email("with.id@example.com")
                .build();

        Employee savedEmployee = Employee.builder()
                .id(3)
                .firstName("With")
                .lastName("ID")
                .email("with.id@example.com")
                .build();

        when(employeeService.save(any(Employee.class))).thenReturn(savedEmployee);

        // When & Then
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeWithId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)));

        // Verify that the ID was set to 0 before saving
        verify(employeeService, times(1)).save(argThat(employee -> employee.getId() == 0));
    }

    @Test
    @DisplayName("PUT /api/employees - Success")
    void testUpdateEmployee() throws Exception {
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

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("PATCH /api/employees/{id} - Success")
    void testPatchEmployee() throws Exception {
        // Given
        Map<String, Object> patchPayload = new HashMap<>();
        patchPayload.put("firstName", "Patched");

        Employee existingEmployee = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        Employee patchedEmployee = Employee.builder()
                .id(1)
                .firstName("Patched")
                .lastName("Doe")
                .email("john@example.com")
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
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        verify(employeeService, times(1)).findById(1);
        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("PATCH /api/employees/{id} - With ID in Payload")
    void testPatchEmployeeWithIdInPayload() throws Exception {
        // Given
        Map<String, Object> patchPayload = new HashMap<>();
        patchPayload.put("id", 100);
        patchPayload.put("firstName", "Patched");

        when(employeeService.findById(1)).thenReturn(employee1);

        // When & Then
        try {
            mockMvc.perform(patch("/api/employees/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(patchPayload)));
        } catch (Exception e) {
            // Expected exception
            assert e.getCause() instanceof RuntimeException;
            assert e.getCause().getMessage().contains("Employee id not allowed in request body - 1");
        }

        verify(employeeService, times(1)).findById(1);
        verify(employeeService, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("PATCH /api/employees/{id} - Employee Not Found")
    void testPatchEmployeeNotFound() throws Exception {
        // Given
        Map<String, Object> patchPayload = new HashMap<>();
        patchPayload.put("firstName", "Patched");

        when(employeeService.findById(999)).thenThrow(new RuntimeException("Did not find employee id - 999"));

        // When & Then
        try {
            mockMvc.perform(patch("/api/employees/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(patchPayload)));
        } catch (Exception e) {
            // Expected exception
            assert e.getCause() instanceof RuntimeException;
            assert e.getCause().getMessage().contains("Did not find employee id - 999");
        }

        verify(employeeService, times(1)).findById(999);
        verify(employeeService, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} - Success")
    void testDeleteEmployee() throws Exception {
        // Given
        when(employeeService.findById(1)).thenReturn(employee1);
        doNothing().when(employeeService).deleteById(1);

        // When & Then
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted employee id - 1"));

        verify(employeeService, times(1)).findById(1);
        verify(employeeService, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} - Not Found")
    void testDeleteEmployeeNotFound() throws Exception {
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
}
