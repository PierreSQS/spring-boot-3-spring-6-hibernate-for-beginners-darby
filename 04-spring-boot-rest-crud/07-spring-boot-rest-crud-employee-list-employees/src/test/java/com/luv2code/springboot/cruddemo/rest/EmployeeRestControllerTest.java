package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.dao.EmployeeDAO;
import com.luv2code.springboot.cruddemo.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeRestController.class)
class EmployeeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeDAO empDAO;

    Employee emp1, emp2;

    @BeforeEach
    void setUp() {
        emp1 = Employee.builder()
                .firstName("emp1")
                .lastName("test1")
                .email("emp1.test1@example.com")
                .build();

        emp2 = Employee.builder()
                .firstName("emp2")
                .lastName("test2")
                .email("emp2.test2@example.com")
                .build();

    }

    @Test
    void findAll() throws Exception {
        // given, when
        given(empDAO.findAll()).willReturn(List.of(emp1,emp2));

        // then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(equalTo(2)))
                .andExpect(jsonPath("$.[0].lastName").value(equalTo("test1")))
                .andDo(print());
    }
}