package com.expense.expense_backend.controller;

import com.expense.expense_backend.entity.User;
import com.expense.expense_backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ Injection par constructeur (BONNE PRATIQUE)
    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= CREATE =================

    @PostMapping
    public User createUser(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // ================= READ =================

    // ✅ Get all users
    @GetMapping
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    // ✅ Get user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setRole(updatedUser.getRole());
        user.setEnabled(updatedUser.isEnabled());


        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(
                    passwordEncoder.encode(updatedUser.getPassword())
            );
        }

        return userRepository.save(user);
    }


    // ================= DELETE =================

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }


}
