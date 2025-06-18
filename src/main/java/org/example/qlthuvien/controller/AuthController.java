package org.example.qlthuvien.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.user.UserResponse;
import org.example.qlthuvien.dto.user.CreateUserRequest;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.mapper.UserMapper;
import org.example.qlthuvien.repository.UserRepository;
import org.example.qlthuvien.utils.JwtUtil;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {
        Map<String, Object> response = new HashMap<>();

        if (request.getEmail() == null || request.getPassword_hash() == null) {
            response.put("success", false);
            response.put("message", "Email and password are required");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.findByEmail(request.getEmail()) != null) {
            response.put("success", false);
            response.put("message", "Email already in use");
            return ResponseEntity.status(409).body(response);
        }

        User user = userMapper.toEntity(request);
        user.setPassword_hash(BCrypt.hashpw(request.getPassword_hash(), BCrypt.gensalt()));

        User savedUser = userRepository.save(user);
        UserResponse userResponse = userMapper.toResponse(savedUser);

        response.put("success", true);
        response.put("message", "User registered successfully");

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CreateUserRequest request, HttpServletResponse servletResponse) {
        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !BCrypt.checkpw(request.getPassword_hash(), user.getPassword_hash())) {
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("None")
                .build();

        servletResponse.addHeader("Set-Cookie", cookie.toString());

        UserResponse userResponse = userMapper.toResponse(user);

        response.put("success", true);
        response.put("message", "Login successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(HttpServletResponse servletResponse) {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        servletResponse.addHeader("Set-Cookie", deleteCookie.toString());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Signed out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@CookieValue(name = "jwt", required = false) String token) {
        Map<String, Object> response = new HashMap<>();

        if (token == null || !jwtUtil.validateToken(token)) {
            response.put("success", false);
            response.put("message", "Invalid or missing token");
            return ResponseEntity.status(401).body(response);
        }

        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email);

        if (user == null) {
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        response.put("success", true);
        response.put("data", userMapper.toResponse(user));
        return ResponseEntity.ok(response);
    }

}
