package com.luv2code.springboot.demosecurity.controller;

import com.luv2code.springboot.demosecurity.security.DemoSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
@Import(value = {DemoSecurityConfig.class})
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void login() throws Exception {
        mockMvc.perform(get("/showMyLoginPage"))
                .andExpect(status().isOk())
                .andExpect(view().name("fancy-login"))
                .andExpect(content().string(containsString("<title>Login Page</title>")))
                .andDo(print());
    }
}