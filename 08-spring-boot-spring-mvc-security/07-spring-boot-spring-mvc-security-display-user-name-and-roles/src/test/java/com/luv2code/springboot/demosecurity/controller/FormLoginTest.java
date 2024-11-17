package com.luv2code.springboot.demosecurity.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class FormLoginTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void formLoginAuthenticated() throws Exception {
        mockMvc.perform(formLogin("/authenticateTheUser")
                        .user("John")
                        .password("John"))
                .andExpect(authenticated())
                .andDo(print());
    }

    @Test
    void formLoginNotAuthenticated() throws Exception {
        mockMvc.perform(formLogin("/authenticateTheUser")
                        .user("Jean")
                        .password("XYxxx"))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/showMyLoginPage?error"))
                .andDo(print());
    }

    @Test
    void formLogout() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/showMyLoginPage?logout"))
                .andDo(print());
    }

    @Test
    void susanAuthenticated() throws Exception {
        mockMvc.perform(get("/showMyLoginPage")
                        .with(user("Susan")))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("User: Susan")))
                .andDo(print());
    }
}