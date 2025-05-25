package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {

    public static final String API_EMPLOYEES_PATH = "/api/employees";
    public static final String MANAGER_ROLE = "MANAGER";

    // add support for JDBC ... no more hardcoded users :-)
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        return new JdbcUserDetailsManager(dataSource);

    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(htc ->
                htc
                        .requestMatchers(HttpMethod.GET, API_EMPLOYEES_PATH+"/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, API_EMPLOYEES_PATH).hasRole(MANAGER_ROLE)
                        .requestMatchers(HttpMethod.PATCH, API_EMPLOYEES_PATH+"/**").hasRole(MANAGER_ROLE)
                        .requestMatchers(HttpMethod.PUT, API_EMPLOYEES_PATH).hasRole(MANAGER_ROLE)
                        .requestMatchers(HttpMethod.DELETE, API_EMPLOYEES_PATH+"/**").hasRole("ADMIN")
        );

        // use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // disable Cross Site Request Forgery (CSRF)
        // in general, not required for stateless REST APIs that use POST, PUT, DELETE and/or PATCH
        http.csrf(CsrfConfigurer::disable);

        return http.build();
    }

}













