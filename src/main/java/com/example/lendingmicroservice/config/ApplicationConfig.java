package com.example.lendingmicroservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApplicationConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/login-to-account", "/start-page").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and().formLogin()
                .loginPage("/login-to-account")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/homepage")
                .and().logout()
                .logoutSuccessUrl("/start-page")
                .and().authenticationManager(authenticationManager(httpSecurity)).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .and().build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        UserDetails user1 = User.withUsername("user111")
                .password(passwordEncoder().encode("123456"))
                .roles("JUN")
                .build();
        UserDetails user2 = User.withUsername("user222")
                .password(passwordEncoder().encode("qwerty543"))
                .roles("JUN")
                .build();
        UserDetails user3 = User.withUsername("mid777")
                .password(passwordEncoder().encode("556677"))
                .roles("MID")
                .build();
        UserDetails user4 = User.withUsername("admin")
                .password(passwordEncoder().encode("951753QQw"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, user2, user3, user4);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
