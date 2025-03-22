package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

    @WebMvcTest(EmployeeRestController.class)
    public class EmployeeRestControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private EmployeeService employeeService;

        @Test
        void findAllEmployees() throws Exception {
            List<Employee> employees = Arrays.asList(new Employee(), new Employee());
            given(employeeService.findAll()).willReturn(employees);

            mockMvc.perform(get("/api/employees"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[{}, {}]"));
        }

        @Test
        void getEmployeeById() throws Exception {
            Employee employee = new Employee();
            given(employeeService.findById(1)).willReturn(employee);

            mockMvc.perform(get("/api/employees/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{}"));
        }

        @Test
        void getEmployeeByIdNotFound() throws Exception {
            given(employeeService.findById(1)).willReturn(null);

            mockMvc.perform(get("/api/employees/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void addNewEmployee() throws Exception {
            Employee employee = new Employee();
            given(employeeService.save(employee)).willReturn(employee);

            mockMvc.perform(post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{}"));
        }

        @Test
        void updateExistingEmployee() throws Exception {
            Employee employee = new Employee();
            given(employeeService.save(employee)).willReturn(employee);

            mockMvc.perform(put("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{}"));
        }
    }