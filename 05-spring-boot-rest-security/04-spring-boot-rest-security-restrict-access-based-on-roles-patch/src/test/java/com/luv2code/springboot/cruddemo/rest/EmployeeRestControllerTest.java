package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeRestController.class)
class EmployeeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EmployeeService employeeService;

    @Autowired
    ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
    }

    @Test
    @DisplayName("GET /employees - Access denied for unauthenticated users")
    void getEmployeesAccessDeniedForUnauthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    @DisplayName("GET /employees - Returns list of employees for EMPLOYEE role")
    void getEmployeesReturnsListForEmployeeRole() throws Exception {
        // Given
        given(employeeService.findAll()).willReturn(List.of(employee));

        // When & Then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("POST /employees - Creates a new employee for MANAGER role")
    void postEmployeeCreatesNewEmployeeForManagerRole() throws Exception {
        // Given
        Employee savedEmployee = new Employee();
        savedEmployee.setId(2);
        savedEmployee.setFirstName("Jane");

        given(employeeService.save(any(Employee.class))).willReturn(savedEmployee);

        // When & Then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    @DisplayName("POST /employees - Access denied for EMPLOYEE role")
    void postEmployeeAccessDeniedForEmployeeRole() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /employees/{id} - Deletes an employee for ADMIN role")
    void deleteEmployeeDeletesEmployeeForAdminRole() throws Exception {
        // Given
        given(employeeService.findById(1)).willReturn(employee);

        // When & Then
        mockMvc.perform(delete("/api/employees/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted employee id - 1"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("DELETE /employees/{id} - Access denied for MANAGER role")
    void deleteEmployeeAccessDeniedForManagerRole() throws Exception {
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("PATCH /employees/{id} - Partially updates an employee for MANAGER role")
    void patchEmployeeUpdatesEmployeeForManagerRole() throws Exception {
        // Given
        Map<String, Object> patchPayload = Map.of("lastName", "Smith");
        Employee patchedEmployee = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Smith")
                .email("john.doe@example.com")
                .build();

        given(employeeService.findById(1)).willReturn(employee);
        given(employeeService.save(any(Employee.class))).willReturn(patchedEmployee);

        // When & Then
        mockMvc.perform(patch("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchPayload))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andDo(print());
    }

    @Disabled("for debugging purposes")
    @Test
    @WithMockUser(roles = "EMPLOYEE")
    @DisplayName("PATCH /employees/{id} - Access denied for EMPLOYEE role")
    void patchEmployeeAccessDeniedForEmployeeRole() throws Exception {
        // Given
        Map<String, Object> patchPayload = Map.of("lastName", "Smith");

        // When & Then
        mockMvc.perform(patch("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchPayload))
                        .with(csrf()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}