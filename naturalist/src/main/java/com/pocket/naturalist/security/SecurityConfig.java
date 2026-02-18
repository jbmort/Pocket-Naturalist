package com.pocket.naturalist.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// sets filters on pages that are restricted from public access such as admin
// sets ability to utilize @preauthorize on park admin controller to restrict access
// to those that are authorized for that particular park

@Configuration
@EnableWebSecurity
@EnableMethodSecurity 
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                            AuthenticationProvider authenticationProvider){
                                this.jwtAuthFilter = jwtAuthenticationFilter;
                                this.authenticationProvider = authenticationProvider;
                            }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for Stateless API
            .authorizeHttpRequests(auth -> auth
                // 1. PUBLIC ROUTES (Login, Public Park Pages)
                .requestMatchers("/auth/**", "/park/**").permitAll()

                // 2. USER ROUTES (Record sightings and checkin)
                .requestMatchers("/sightings/**", "/checkin/**").hasAnyRole("ADMIN", "USER")
                
                // 3. ADMIN ROUTES (Broad protection)
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // 4. USER ROUTES (Check-ins, Sightings)
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}