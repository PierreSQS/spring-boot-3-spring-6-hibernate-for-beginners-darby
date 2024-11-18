package com.luv2code.springboot.demosecurity.controller;

import com.luv2code.springboot.demosecurity.security.DemoSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.AdditionalMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DemoController.class)
@Import(value = {DemoSecurityConfig.class})
class DemoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void showHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/showMyLoginPage"))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = "Mary")
    void showLeaders() throws Exception{
        mockMvc.perform(get("/leaders"))
                .andExpect(status().isOk())
                .andExpect(view().name("leaders"))
                .andExpect(content().string(containsString("<title>luv2code LEADERS Home Page</title>")))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = "Susan")
    void showAdmins() throws Exception{
        mockMvc.perform(get("/systems"))
                .andExpect(status().isOk())
                .andExpect(view().name("systems"))
                .andExpect(content().string(containsString("<title>luv2code SYSTEMS Home Page</title>")))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = "John")
    void johnNotAllowedOnAdminPage() throws Exception{
        mockMvc.perform(get("/systems"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = "John")
    void johnNotAllowedOnManagerPage() throws Exception{
        mockMvc.perform(get("/leaders"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = "Mary")
    void maryNotAllowedOnAdminPage() throws Exception{
        mockMvc.perform(get("/systems"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    // Roles based Content Tests
    @Test
    @WithMockUser(username = "MockUser", roles = {"EMPLOYEE"})
    void employeeRoleDoesNotSeeManagersAndLeadersLink() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString(not("<title>luv2code SYSTEMS Home Page</title>"))))
                .andExpect(content().string(
                        containsString(not("<title>luv2code LEADERS Home Page</title>"))))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "MockUser", roles = {"MANAGER"})
    void managerRoleDoesNotSeeLeadersLink() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString(not("<title>luv2code LEADERS Home Page</title>"))))
                .andDo(print());
    }
}