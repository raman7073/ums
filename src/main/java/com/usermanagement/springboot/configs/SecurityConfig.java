package com.usermanagement.springboot.configs;

import com.usermanagement.springboot.security.CustomUserDetailsService;
import com.usermanagement.springboot.security.JwtAuthenticationEntryPoint;
import com.usermanagement.springboot.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.usermanagement.springboot.common.Constants.LOGIN_API;

/**
 * Here, @SecurityConfig class is responsible for configuring the security which ensures
 * that only authenticated users can see the secret greeting
 * The SecurityConfig class is annotated with @EnableWebSecurity to enable Spring Security’s
 * web security support and provide the Spring MVC integration. It also exposes four beans to set
 * some specifics for the web security configuration.
 * The @SecurityFilterChain bean defines which URL paths should be secured and which should not.
 * Specifically, the login paths are configured to not require any authentication.
 * All other paths must be authenticated.
 *
 * Inside @SecurityFilterChain , the exception handling code ensures that the server will return
 * HTTP status 401 (Unauthorized) if any error occurs during authentication process.
 */

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers(LOGIN_API)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(daoAuthenticationProvider())
                .build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
}
