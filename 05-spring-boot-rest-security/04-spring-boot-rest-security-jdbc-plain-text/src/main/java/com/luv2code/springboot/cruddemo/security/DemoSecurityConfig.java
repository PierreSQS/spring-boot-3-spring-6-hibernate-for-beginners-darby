package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    public static final String EMPLOYEE_ROLE = "EMPLOYEE";
    public static final String MANAGER_ROLE = "MANAGER";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String EMPLOYEE_PATH = "/api/employees";

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails john = User.builder()
                .username("John")
                .password("{noop}John")
                .roles(EMPLOYEE_ROLE)
                .build();

        UserDetails mary = User.builder()
                .username("Mary")
                .password("{noop}Mary")
                .roles(EMPLOYEE_ROLE, MANAGER_ROLE)
                .build();

        UserDetails susan = User.builder()
                .username("Susan")
                .password("{noop}Susan")
                .roles(EMPLOYEE_ROLE, MANAGER_ROLE, ADMIN_ROLE)
                .build();

        return new InMemoryUserDetailsManager(john, mary, susan);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, EMPLOYEE_PATH).hasRole(EMPLOYEE_ROLE)
                .requestMatchers(HttpMethod.GET, EMPLOYEE_PATH+"/**").hasRole(EMPLOYEE_ROLE)
                .requestMatchers(HttpMethod.POST, EMPLOYEE_PATH).hasRole(MANAGER_ROLE)
                .requestMatchers(HttpMethod.PUT, EMPLOYEE_PATH).hasRole(MANAGER_ROLE)
                .requestMatchers(HttpMethod.DELETE, EMPLOYEE_PATH+"/**").hasRole(ADMIN_ROLE));

        // Basic Authentication
        http.httpBasic(Customizer.withDefaults());

        return http.build();

    }
}













