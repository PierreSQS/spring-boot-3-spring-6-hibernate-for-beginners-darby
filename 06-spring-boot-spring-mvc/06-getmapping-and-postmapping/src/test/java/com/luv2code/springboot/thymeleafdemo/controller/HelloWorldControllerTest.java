package com.luv2code.springboot.thymeleafdemo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HelloWorldController.class)
class HelloWorldControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void showForm() throws Exception {
        mockMvc.perform(get("/showForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("helloworld-form"))
                .andExpect(content().string(containsString("<title>Hello World - Input Form</title>")))
                .andDo(print());
    }

    @Test
    void letsShoutDude() throws Exception {
        mockMvc.perform(get("/processFormVersionTwo").param("studentName","Pierrot"))
                .andExpect(status().isOk())
                .andExpect(view().name("helloworld"))
                .andExpect(model().attribute("message", "Yo! PIERROT"))
                .andExpect(content().string(containsString("Yo! PIERROT")))
                .andDo(print());
    }

    @Test
    void processFormVersionThree() throws Exception {
        mockMvc.perform(get("/processFormVersionThree").param("studentName", "Odile"))
                .andExpect(status().isOk())
                .andExpect(view().name("helloworld"))
                .andExpect(model().attribute("message", "Hey My Friend from v3! ODILE"))
                .andExpect(content().string(containsString("Odile")))
                .andDo(print());
    }
}