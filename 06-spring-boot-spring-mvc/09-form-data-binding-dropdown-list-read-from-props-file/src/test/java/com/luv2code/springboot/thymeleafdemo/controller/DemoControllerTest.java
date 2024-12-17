package com.luv2code.springboot.thymeleafdemo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = {DemoController.class})
class DemoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void sayHello() throws Exception {
        mockMvc.perform(get("/hello?studentName={name}","Pierrot"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("theDate"))
                .andExpect(view().name("helloworld"))
                .andExpect(content().string(containsString("Student name: <span >Pierrot</span>")))
                .andDo(print());
    }
}