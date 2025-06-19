package com.luv2code.springboot.cruddemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springboot.cruddemo.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_PATH = "/magic-api/employees";

    @Test
    public void testGetAllEmployees() throws Exception {
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.employees", isA(java.util.List.class)));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        MvcResult result = mockMvc.perform(post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        // Verify the Location header points to a valid resource
        String location = result.getResponse().getHeader("Location");
        Assertions.assertNotNull(location);
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andDo(print());
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        // First, create an employee
        Employee employee = Employee.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        MvcResult result = mockMvc.perform(post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        // Extract the location header which contains the URL to the created resource
        String location = result.getResponse().getHeader("Location");
        Assertions.assertNotNull(location);
        loggingLocation(location);

        // Now get the employee by ID
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.email", is("jane.smith@example.com")))
                .andDo(print());
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        // First, create an employee
        Employee employee = Employee.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .email("bob.johnson@example.com")
                .build();

        MvcResult result = mockMvc.perform(post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        // Extract the location header which contains the URL to the created resource
        String location = result.getResponse().getHeader("Location");
        Assertions.assertNotNull(location);
        loggingLocation(location);

        // Update the employee
        employee.setFirstName("Robert");
        employee.setEmail("robert.johnson@example.com");

        // Perform the update
        mockMvc.perform(put(location)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isNoContent());  // Spring Data REST returns 204 No Content for successful PUT

        // Verify the update was successful by getting the employee
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.firstName", is("Robert")))
                .andExpect(jsonPath("$.lastName", is("Johnson")))
                .andExpect(jsonPath("$.email", is("robert.johnson@example.com")));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        // First, create an employee
        Employee employee = Employee.builder()
                .firstName("Alice")
                .lastName("Brown")
                .email("alice.brown@example.com")
                .build();

        MvcResult result = mockMvc.perform(post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract the location header which contains the URL to the created resource
        String location = result.getResponse().getHeader("Location");
        Assertions.assertNotNull(location);
        loggingLocation(location);

        // Delete the employee
        mockMvc.perform(delete(location))
                .andExpect(status().isNoContent());

        // Verify the employee is deleted
        mockMvc.perform(get(location))
                .andExpect(status().isNotFound());
    }

    private static void loggingLocation(String location) {
        log.info("###### Location: {} #######", location);
    }

}
