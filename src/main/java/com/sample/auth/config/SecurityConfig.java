package com.sample.auth.config;

import com.sample.auth.filter.JwtFilter;
import com.sample.auth.service.JwtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUserService jwtUserService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(jwtUserService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()  // Start authorization rules
                .requestMatchers("/User/authenticate", "/User/signup", "/User/checkUserMailId/**", "/User/checkUsername/**")  // URL patterns to allow without authentication
                .permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN") // Allow only ADMIN role to access /admin/**
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // Allow USER and ADMIN roles to access /user/**
                .anyRequest().authenticated()  // Any other request requires authentication
                .and()
                .exceptionHandling()  // Optional: Customize exception handling
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);  // Disable session management for JWT authentication

        // Add JWT filter before the default UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // CORS configuration to allow cross-origin requests from localhost:3000
        http.cors().configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
//            config.addAllowedOrigin("http://localhost:3000");
            config.addAllowedOrigin("https://audit-tracker.netlify.app");
            config.setAllowCredentials(true);
            return config;
        });

        return http.build();  // Return the configured filter chain
    }


}