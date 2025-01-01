package com.luv2code.springboot.thymeleafdemo.controller;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void saveEmployee() throws Exception {
        // Given
        Employee empToSave = Employee.builder()
                .firstName("Test")
                .lastName("To Save")
                .email("test.tosave@example.com")
                .build();

        MultiValueMap<String, String> empToSaveMap = new LinkedMultiValueMap<>();
        empToSaveMap.add("firstName", empToSave.getFirstName());
        empToSaveMap.add("lastName", empToSave.getLastName());
        empToSaveMap.add("email", empToSave.getEmail());

        // When, Then
        mockMvc.perform(post("/employees/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(empToSaveMap))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employees/list"))
                .andDo(print());

        verify(empServMock,times(1)).save(empToSave);
    }

    @Test
    void showFormForUpdate() throws Exception {
        // Given
        given(empServMock.findById(anyInt())).willReturn(emp1);

        // When, then
        mockMvc.perform(get("/employees/showFormForUpdate")
                        .param("employeeId", String.valueOf(emp1.getId())))
                .andExpect(status().isOk())
                .andExpect(model().attribute("employee", emp1))
                .andExpect(view().name("employees/employee-form"))
                .andExpect(content().string(containsString("<title>Save Employee</title>")))
                .andDo(print());
    }

    @Test
    void deleteEmployee() throws Exception {
        // Given
        Employee empToDelete = Employee.builder()
                .id(1)
                .firstName("Employee")
                .lastName("To Delete")
                .email("employee.todele@luv2code.com")
                .build();

        given(empServMock.findById(anyInt())).willReturn(empToDelete);

        mockMvc.perform(get("/employees/delete?employeeId={empID}" ,empToDelete.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("tempEmployee", empToDelete))
                .andDo(print());

        verify(empServMock).deleteById(empToDelete.getId());
    }

}