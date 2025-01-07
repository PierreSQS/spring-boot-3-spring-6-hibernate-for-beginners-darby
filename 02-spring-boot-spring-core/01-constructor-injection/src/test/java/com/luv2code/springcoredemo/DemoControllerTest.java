package com.luv2code.springcoredemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DemoController.class)
class DemoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    Coach coachMock;

    @Test
    void getDailyWorkout() throws Exception {
        // Given
        given(coachMock.getDailyWorkout()).willReturn("Success!!!");

        mockMvc.perform(get("/dailyworkout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success!!!"))
                .andDo(print());
    }
}