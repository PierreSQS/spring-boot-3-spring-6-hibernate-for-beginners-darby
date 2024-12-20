package com.luv2code.springdemo.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(controllers = {CustomerController.class})
class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;

    Customer customer1;

    @BeforeEach
    void setUp() {
        customer1 = Customer.builder()
                .firstName("Test")
                .lastName("User")
                .build();
    }

    @Test
    void showForm() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customer", new Customer()))
                .andExpect(view().name("customer-form"))
                .andExpect(content().string(containsString("<title>Customer Registration Form</title>")))
                .andDo(print());
    }

    @Test
    void processFormValidCustomer() throws Exception {
        mockMvc.perform(post("/processForm")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("firstName="+customer1.getFirstName()+"&lastName="+customer1.getLastName()))
                .andExpect(status().isOk())
                .andExpect(view().name("customer-confirmation"))
                .andDo(print());
    }
}