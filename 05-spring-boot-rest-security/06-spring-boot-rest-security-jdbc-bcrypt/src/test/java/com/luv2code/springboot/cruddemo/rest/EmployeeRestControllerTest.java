package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objMapper;

    @MockitoBean
    EmployeeService employeeService;

    Employee emp1, emp2;

    @BeforeEach
    void setUp() {
        emp1 = new Employee(1, "Max", "Mustermann", "max@firma.de");
        emp2 = new Employee(2, "Erika", "Musterfrau", "erika@firma.de");

    }

    @WithMockUser(username = "max", roles = {"EMPLOYEE"})
    @Test
    void testFindAllEmployees() throws Exception {

        given(employeeService.findAll()).willReturn(List.of(emp1, emp2));

        mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Max"))
                .andExpect(jsonPath("$[1].firstName").value("Erika"));
    }

    @WithMockUser(username = "max", roles = {"EMPLOYEE"})
    @Test
    void testGetEmployeeById() throws Exception {
        given(employeeService.findById(1)).willReturn(emp1);

        mockMvc.perform(get("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Max"));
    }

    @WithMockUser(username = "max", roles = {"MANAGER"})
    @Test
    void testAddEmployee() throws Exception {
        Employee empToSave = Employee.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .email("max@firma.de")
                .build();

        Employee savedEmp = emp1;
        given(employeeService.save(any(Employee.class))).willReturn(savedEmp);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objMapper.writeValueAsString(empToSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andDo(print());
    }

    @WithMockUser(username = "max", roles = {"MANAGER"})
    @Test
    void testUpdateEmployee() throws Exception {
        Employee empToUpdate = new Employee(1, "Maximilian", "Mustermann", "maximilian@firma.de");
        given(employeeService.save(Mockito.any(Employee.class))).willReturn(emp1);

        mockMvc.perform(put("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objMapper.writeValueAsString(empToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Max"))
                .andDo(print());
    }

    @WithMockUser(username = "max", roles = {"MANAGER"})
    @Test
    void testPatchEmployee() throws Exception {
        Employee patchedEmp = new Employee(1, "Maximilian", "Mustermann", "max@firma.de");
        Map<String, Object> patchPayload = new HashMap<>();
        patchPayload.put("firstName", "Maximilian");

        given(employeeService.findById(1)).willReturn(emp1);
        given(employeeService.save(Mockito.any(Employee.class))).willReturn(patchedEmp);

        mockMvc.perform(patch("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objMapper.writeValueAsString(patchPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Maximilian"));
    }

    @WithMockUser(username = "max", roles = {"ADMIN"})
    @Test
    void testDeleteEmployee() throws Exception {
        given(employeeService.findById(1)).willReturn(emp1);

        mockMvc.perform(delete("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted employee id - 1"));
    }
}