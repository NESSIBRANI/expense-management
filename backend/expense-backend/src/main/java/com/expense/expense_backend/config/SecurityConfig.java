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

    // ========== PUBLIC ==========
    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    .requestMatchers("/error").permitAll()

    .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
    .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()

    // connecté
    .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()


    // ========== EMPLOYEE ==========
    .requestMatchers(HttpMethod.POST, "/api/reports").hasRole("EMPLOYEE")
    .requestMatchers(HttpMethod.GET,  "/api/reports/my/**").hasRole("EMPLOYEE")
    .requestMatchers(HttpMethod.PUT,  "/api/reports/*/submit").hasRole("EMPLOYEE")
    .requestMatchers(HttpMethod.DELETE, "/api/reports/*").hasRole("EMPLOYEE")

    .requestMatchers("/api/expenses/**").hasRole("EMPLOYEE")


    // ========== MANAGER ==========
    .requestMatchers(HttpMethod.GET, "/api/reports/pending").hasRole("MANAGER")
    .requestMatchers(HttpMethod.PUT, "/api/reports/*/approve").hasRole("MANAGER")
    .requestMatchers(HttpMethod.PUT, "/api/reports/*/reject").hasRole("MANAGER")


    // ========== ADMIN ==========
    .requestMatchers("/api/admin/**").hasRole("ADMIN")

    // tout le reste
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
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}