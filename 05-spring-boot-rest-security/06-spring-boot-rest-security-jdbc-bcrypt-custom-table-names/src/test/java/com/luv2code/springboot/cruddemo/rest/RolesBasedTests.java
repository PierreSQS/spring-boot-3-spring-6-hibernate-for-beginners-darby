package com.luv2code.springboot.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.luv2code.springboot.cruddemo.security.DemoSecurityConfig.EMPLOYEE_ROLE;
import static com.luv2code.springboot.cruddemo.security.DemoSecurityConfig.MANAGER_ROLE;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RolesBasedTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeServiceImpl empServ;

    @Autowired
    ObjectMapper objectMapper;

    Employee emp1, emp2;

    @BeforeEach
    void setUp() {
        emp1 = Employee.builder()
                .firstName("Hector")
                .lastName("Perez")
                .email("hector.perez@example.com")
                .build();

        emp2 = Employee.builder()
                .id(6)
                .firstName("Hector")
                .lastName("Fernandez")
                .email("hector.fernandez@example.com")
                .build();
    }

    @WithMockUser(username = "MockUser", roles = {EMPLOYEE_ROLE})
    @Test
    void roleEmployeeCanGetAllEmployees() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @WithMockUser(username = "MockUser", roles = {EMPLOYEE_ROLE})
    @Test
    void roleEmployeeCanGetEmployee() throws Exception {

        Employee firstFoundEmp = empServ.findAll().getFirst();

        mockMvc.perform(get("/api/employees/{empID}",firstFoundEmp.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(equalTo("Leslie")))
                .andDo(print());
    }

    @WithMockUser(username = "MockUser", roles = {MANAGER_ROLE})
    @Test
    void roleManagerCanAddEmployee() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp1)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @WithMockUser(username = "MockUser", roles = {MANAGER_ROLE})
    @Test
    void roleManagerCanUpdateEmployee() throws Exception {
        mockMvc.perform(put("/api/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp2)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @WithMockUser(username = "MockUser", roles = {EMPLOYEE_ROLE})
    @Test
    void roleEmployeeCanNotAddEmployee() throws Exception {
        mockMvc.perform(post("/api/employees"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @WithMockUser(username = "MockUser", roles = {EMPLOYEE_ROLE})
    @Test
    void roleEmployeeCanNotUpdateEmployee() throws Exception {
        mockMvc.perform(put("/api/employees"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @WithMockUser(username = "MockUser", roles = {MANAGER_ROLE})
    @Test
    void roleManagerCanNotDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @WithMockUser(username = "MockUser", roles = {EMPLOYEE_ROLE})
    @Test
    void roleEmployeeCanNotDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

}
