package com.luv2code.springboot.demosecurity.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DemoSecurityConfig {

    public static final String EMPLOYEE_ROLE = "EMPLOYEE";
    public static final String MANAGER_ROLE = "MANAGER";
    public static final String ADMIN_ROLE = "ADMIN";

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
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

//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//
//        UserDetails john = User.builder()
//                .username("John")
//                .password("{noop}John")
//                .roles(EMPLOYEE_ROLE)
//                .build();
//
//        UserDetails mary = User.builder()
//                .username("Mary")
//                .password("{noop}Mary")
//                .roles(EMPLOYEE_ROLE, MANAGER_ROLE)
//                .build();
//
//        UserDetails susan = User.builder()
//                .username("Susan")
//                .password("{noop}Susan")
//                .roles(EMPLOYEE_ROLE, MANAGER_ROLE, ADMIN_ROLE)
//                .build();
//
//        return new InMemoryUserDetailsManager(john, mary, susan);
//    }









