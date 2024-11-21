package com.luv2code.demo.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DemoRestController.class)
class DemoRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void sayHello() throws Exception {
        mockMvc.perform(get("/test/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello World!")))
                .andDo(print());
    }
}