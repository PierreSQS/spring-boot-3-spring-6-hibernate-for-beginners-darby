package com.luv2code.springdemo.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
                .freePasses(10)
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
        MultiValueMap<String,String> formFields = new LinkedMultiValueMap<>();
        formFields.add("firstName", customer1.getFirstName());
        formFields.add("lastName", customer1.getLastName());
        formFields.add("freePasses", Integer.toString(customer1.getFreePasses()));

        mockMvc.perform(post("/processForm")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(formFields))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customer",customer1))
                .andExpect(view().name("customer-confirmation"))
                .andExpect(content().string(containsString("The customer is confirmed: <span >Test User</span>")))
                .andExpect(content().string(containsString("Free passes: <span >10</span>")))
                .andDo(print());
    }

    @Test
    void processFormCustomerNotValid() throws Exception {
        // field customer lastName is not submitted, freePass=11
        mockMvc.perform(post("/processForm")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("firstName="+customer1.getFirstName()+"&freePasses=11"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("customer","lastName","Size"))
                .andExpect(model().attributeHasFieldErrorCode("customer","freePasses","Max"))
                .andExpect(view().name("customer-form"))
                .andExpect(content().string(
                        containsString("<span class=\"error\">is required</span>")))
                .andExpect(content().string(
                        containsString("<span class=\"error\">must be less than or equal to 10</span>")))
                .andDo(print());
    }

    @Test
    void processFormCustomerNotValid2() throws Exception {
        // field customer lastName is not submitted, freePass=-1
        mockMvc.perform(post("/processForm")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("firstName="+customer1.getFirstName()+"&freePasses=-1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("customer","lastName","Size"))
                .andExpect(model().attributeHasFieldErrorCode("customer","freePasses","Min"))
                .andExpect(view().name("customer-form"))
                .andExpect(content().string(
                        containsString("<span class=\"error\">is required</span>")))
                .andExpect(content().string(
                        containsString("<span class=\"error\">must be greater than or equal to zero</span>")))
                .andDo(print());
    }

    @Test
    void processFormTestInitBinder() throws Exception {
        // BLANKS IN FIELD LASTNAME ARE NOT ALLOWED!!!!!!!1
        mockMvc.perform(post("/processForm")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("firstName="+customer1.getFirstName()+"&lastName=  "))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("customer"))
                .andExpect(view().name("customer-form"))
                .andExpect(content().string(containsString("<title>Customer Registration Form</title>")))
                .andDo(print());
    }
}