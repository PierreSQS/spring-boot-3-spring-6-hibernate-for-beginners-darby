package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    public static final String EMP_ROLE = "EMPLOYEE";
    public static final String MGR_ROLE = "MANAGER";

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails john = User.builder()
                .username("john")
                .password("{noop}john")
                .roles(EMP_ROLE)
                .build();

        UserDetails mary = User.builder()
                .username("mary")
                .password("{noop}mary")
                .roles(EMP_ROLE, MGR_ROLE)
                .build();

        UserDetails susan = User.builder()
                .username("susan")
                .password("{noop}susan")
                .roles(EMP_ROLE, MGR_ROLE, "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(john, mary, susan);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole(EMP_ROLE)
                        .requestMatchers(HttpMethod.POST, "/api/employees").hasRole(MGR_ROLE)
                        .requestMatchers(HttpMethod.PUT, "/api/employees").hasRole(MGR_ROLE)
                        .requestMatchers(HttpMethod.PATCH, "/api/employees/**").hasRole(MGR_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
        );

        // use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // disable Cross Site Request Forgery (CSRF)
        // in general, not required for stateless REST APIs that use POST, PUT, DELETE and/or PATCH
        http.csrf(CsrfConfigurer::disable);

        return http.build();
    }
}













