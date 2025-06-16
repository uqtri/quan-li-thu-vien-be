package org.example.qlthuvien.controller;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.repository.UserRepository;
import org.example.qlthuvien.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getAllUsers(@CookieValue(name = "jwt", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        if (!hasRole(token, "ADMIN")) {
            response.put("success", false);
            response.put("message", "Access denied");
            return ResponseEntity.status(403).body(response);
        }

        response.put("success", true);
        response.put("data", userRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, @CookieValue(name = "jwt", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        if (token == null || !jwtUtil.validateToken(token)) {
            response.put("success", false);
            response.put("message", "Invalid or missing token");
            return ResponseEntity.status(401).body(response);
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        User user = userOpt.get();
        String requesterEmail = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        if (!user.getEmail().equals(requesterEmail) && !"ADMIN".equals(role)) {
            response.put("success", false);
            response.put("message", "Access denied");
            return ResponseEntity.status(403).body(response);
        }

        response.put("success", true);
        response.put("data", user);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user, @CookieValue(name = "jwt", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        if (!hasRole(token, "ADMIN")) {
            response.put("success", false);
            response.put("message", "Access denied");
            return ResponseEntity.status(403).body(response);
        }

        if (user.getEmail() == null || user.getPassword_hash() == null) {
            response.put("success", false);
            response.put("message", "Email and password are required");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            response.put("success", false);
            response.put("message", "Email already in use");
            return ResponseEntity.status(409).body(response);
        }

        user.setPassword_hash(BCrypt.hashpw(user.getPassword_hash(), BCrypt.gensalt()));
        User savedUser = userRepository.save(user);

        response.put("success", true);
        response.put("data", savedUser);
        response.put("message", "User created successfully");
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody User updatedUser,
                                        @CookieValue(name = "jwt", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        if (token == null || !jwtUtil.validateToken(token)) {
            response.put("success", false);
            response.put("message", "Invalid or missing token");
            return ResponseEntity.status(401).body(response);
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        User existingUser = userOpt.get();
        String requesterEmail = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        if (!existingUser.getEmail().equals(requesterEmail) && !"ADMIN".equals(role)) {
            response.put("success", false);
            response.put("message", "Access denied");
            return ResponseEntity.status(403).body(response);
        }

        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }

        if (updatedUser.getEmail() != null) {
            User userWithSameEmail = userRepository.findByEmail(updatedUser.getEmail());
            if (userWithSameEmail != null && !userWithSameEmail.getId().equals(id)) {
                response.put("success", false);
                response.put("message", "Email already in use");
                return ResponseEntity.status(409).body(response);
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

        User savedUser = userRepository.save(existingUser);
        response.put("success", true);
        response.put("data", savedUser);
        response.put("message", "User updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @CookieValue(name = "jwt", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        if (!hasRole(token, "ADMIN")) {
            response.put("success", false);
            response.put("message", "Access denied");
            return ResponseEntity.status(403).body(response);
        }

        if (!userRepository.existsById(id)) {
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        userRepository.deleteById(id);
        response.put("success", true);
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }

    private boolean hasRole(String token, String requiredRole) {
        return token != null && jwtUtil.validateToken(token) && requiredRole.equals(jwtUtil.extractRole(token));
    }
}
