package com.luv2code.springboot.thymeleafdemo.controller;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EmployeeService empServMock;

    // Mocks
    Employee emp1, emp2;


    @BeforeEach
    void setUp() {
        emp1 = Employee.builder()
                .id(1)
                .firstName("Test1")
                .lastName("User1")
                .build();

        emp2 = Employee.builder()
                .id(2)
                .firstName("Test2")
                .lastName("User2")
                .build();
    }


    @Test
    void listEmployees() throws Exception {
        // Given
        List<Employee> employeeList = List.of(emp1, emp2);
        given(empServMock.findAll()).willReturn(employeeList);

        // When, Then
        mockMvc.perform(get("/employees/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/list-employees"))
                .andExpect(model().attribute("employees", employeeList))
                .andExpect(content().string(containsString("<title>Employee Directory</title>")))
                .andDo(print());
    }

    @Test
    void showFormForAdd() throws Exception {
        // When, Then
        mockMvc.perform(get("/employees/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/employee-form"))
                .andExpect(model().attribute("employee", new Employee()))
                .andExpect(content().string(containsString("<title>Save Employee</title>")))
                .andDo(print());
    }
}