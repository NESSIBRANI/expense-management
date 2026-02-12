package com.expense.expense_backend.config;

import com.expense.expense_backend.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import java.util.List;


@Configuration
@EnableWebSecurity

public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

  @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())

     .authorizeHttpRequests(auth -> auth

    // CORS
    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

    // SPRING INTERNAL
    .requestMatchers("/error").permitAll()

    // AUTH
    .requestMatchers("/auth/login", "/auth/register").permitAll()
    .requestMatchers("/auth/me").authenticated()

    // USERS
    .requestMatchers("/api/users/**").authenticated()

    // EMPLOYEE
    .requestMatchers(HttpMethod.GET, "/api/expenses/my/**")
        .hasRole("EMPLOYEE")

    .requestMatchers(HttpMethod.POST, "/api/expenses/report/**")
        .hasRole("EMPLOYEE")

    // MANAGER
    .requestMatchers(HttpMethod.PUT, "/api/expenses/**")
        .hasRole("MANAGER")

    .requestMatchers(HttpMethod.DELETE, "/api/expenses/**")
        .hasRole("MANAGER")

    .anyRequest().authenticated()
)



        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .httpBasic(basic -> basic.disable())
        .formLogin(form -> form.disable())

        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Needed for authentication
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}