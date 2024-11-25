package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {

    public static final String EMPLOYEE_ROLE = "EMPLOYEE";
    public static final String MANAGER_ROLE = "MANAGER";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String EMPLOYEE_PATH = "/api/employees";
    public static final String FROM_MEMBERS_BY_USER_ID = "select user_id, pw, active  from members where user_id = ?";
    public static final String ROLES_BY_USER_ID = "select user_id, role  from roles where user_id = ?";

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(FROM_MEMBERS_BY_USER_ID);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(ROLES_BY_USER_ID);
        return jdbcUserDetailsManager;
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

        // Disable CSRF since API
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();

    }
}













