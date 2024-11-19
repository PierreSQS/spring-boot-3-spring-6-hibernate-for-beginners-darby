package com.luv2code.springboot.demosecurity.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DemoSecurityConfig {

    public static final String EMPLOYEE_ROLE = "EMPLOYEE";
    public static final String MANAGER_ROLE = "MANAGER";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String MEMBER_BY_USER_ID = "SELECT user_id, pw, active FROM members WHERE user_id=?";
    public static final String ROLE_BY_USER_ID = "SELECT user_id, role FROM roles WHERE user_id=?";

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(MEMBER_BY_USER_ID);

        // define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(ROLE_BY_USER_ID);

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").hasRole(EMPLOYEE_ROLE)
                        .requestMatchers("/leaders/**").hasRole(MANAGER_ROLE)
                        .requestMatchers("/systems/**").hasRole(ADMIN_ROLE)
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/showMyLoginPage")
                        .loginProcessingUrl("/authenticateTheUser") // allows the authentication automatically
                        .permitAll())
                .logout(LogoutConfigurer::permitAll)
                // configured the access denied Page
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied") );

        return http.build();
    }
}









