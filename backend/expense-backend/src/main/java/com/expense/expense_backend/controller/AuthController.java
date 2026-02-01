package com.expense.expense_backend.controller;

import com.expense.expense_backend.dto.LoginRequest;
import com.expense.expense_backend.entity.User;
import  com.expense.expense_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.expense.expense_backend.security.JwtUtil;
import java.util.Map;
import com.expense.expense_backend.entity.Role;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
        user.setRole(Role.EMPLOYEE);

        user.setEnabled(true);


        return userRepository.save(user);
    }


    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        User dbUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(dbUser.getEmail(),dbUser.getRole().name());

        return Map.of("token", token);
    }
    @GetMapping("/me")
    public User me(Authentication auth) {

        return userRepository
                .findByEmail(auth.getName())
                .orElseThrow();
    }

}
