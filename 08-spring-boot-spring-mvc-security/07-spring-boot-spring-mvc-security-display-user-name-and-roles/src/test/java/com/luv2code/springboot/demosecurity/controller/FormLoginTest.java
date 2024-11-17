package com.luv2code.springboot.demosecurity.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}