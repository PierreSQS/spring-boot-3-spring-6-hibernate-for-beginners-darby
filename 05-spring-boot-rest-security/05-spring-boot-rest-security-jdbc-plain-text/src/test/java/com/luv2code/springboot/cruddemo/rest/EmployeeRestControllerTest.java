// Datei: src/test/java/com/luv2code/springboot/cruddemo/rest/EmployeeRestControllerTest.java
package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeRestController.class)
class EmployeeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ObjectMapper objectMapper;

    @Test
    void testFindAllEmployees() throws Exception {
        Employee emp1 = new Employee(1, "Max", "Mustermann", "max@firma.de");
        Employee emp2 = new Employee(2, "Erika", "Musterfrau", "erika@firma.de");
        Mockito.when(employeeService.findAll()).thenReturn(Arrays.asList(emp1, emp2));

        mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Max"))
                .andExpect(jsonPath("$[1].firstName").value("Erika"));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        Employee emp = new Employee(1, "Max", "Mustermann", "max@firma.de");
        Mockito.when(employeeService.findById(1)).thenReturn(emp);

        mockMvc.perform(get("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Max"));
    }

    @Test
    void testAddEmployee() throws Exception {
        Employee emp = new Employee(0, "Max", "Mustermann", "max@firma.de");
        Employee savedEmp = new Employee(1, "Max", "Mustermann", "max@firma.de");
        Mockito.when(employeeService.save(Mockito.any(Employee.class))).thenReturn(savedEmp);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Max\",\"lastName\":\"Mustermann\",\"email\":\"max@firma.de\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        Employee emp = new Employee(1, "Max", "Mustermann", "max@firma.de");
        Mockito.when(employeeService.save(Mockito.any(Employee.class))).thenReturn(emp);

        mockMvc.perform(put("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"firstName\":\"Max\",\"lastName\":\"Mustermann\",\"email\":\"max@firma.de\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testPatchEmployee() throws Exception {
        Employee emp = new Employee(1, "Max", "Mustermann", "max@firma.de");
        Employee patchedEmp = new Employee(1, "Maximilian", "Mustermann", "max@firma.de");
        Map<String, Object> patchPayload = new HashMap<>();
        patchPayload.put("firstName", "Maximilian");

        Mockito.when(employeeService.findById(1)).thenReturn(emp);
        Mockito.when(employeeService.save(Mockito.any(Employee.class))).thenReturn(patchedEmp);

        mockMvc.perform(patch("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Maximilian\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Maximilian"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        Employee emp = new Employee(1, "Max", "Mustermann", "max@firma.de");
        Mockito.when(employeeService.findById(1)).thenReturn(emp);

        mockMvc.perform(delete("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted employee id - 1"));
    }
}