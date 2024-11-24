package com.luv2code.springboot.cruddemo.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class LoginTests {

    @Autowired
    MockMvc mockMvc;

    @WithUserDetails("John")
    @Test
    void johnLoginSucceededOnGetAllEmployees() throws Exception {

        mockMvc.perform(get("/api/employees"))
                .andExpect(authenticated())
                .andDo(print());

    }

    @WithUserDetails("Mary")
    @Test
    void maryLoginSucceededOnGetAllEmployees() throws Exception {

        mockMvc.perform(get("/api/employees"))
                .andExpect(authenticated())
                .andDo(print());

    }


    @WithUserDetails("Susan")
    @Test
    void susanLoginSucceededOnGetAllEmployees() throws Exception {

        mockMvc.perform(get("/api/employees"))
                .andExpect(authenticated())
                .andDo(print());

    }

    @Test
    void BadCredentials() throws Exception {
        mockMvc.perform(get("/api/employees")
                        .with(httpBasic("Johni", "John")))
                .andExpect(unauthenticated())
                .andDo(print());
    }
}
