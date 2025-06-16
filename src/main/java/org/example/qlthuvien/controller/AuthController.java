package org.example.qlthuvien.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.entity.User;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        if (user.getEmail() == null || user.getPassword_hash() == null) {
            response.put("success", false);
            response.put("message", "Email and password are required");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            response.put("success", false);
            response.put("message", "Email already in use");
            return ResponseEntity.badRequest().body(response);
        }

        user.setPassword_hash(BCrypt.hashpw(user.getPassword_hash(), BCrypt.gensalt()));

        User savedUser = userRepository.save(user);

        response.put("success", true);
        response.put("message", "User registered successfully");
        response.put("data", savedUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginReq, HttpServletResponse servletResponse) {
        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByEmail(loginReq.getEmail());
        if (user == null || !BCrypt.checkpw(loginReq.getPassword_hash(), user.getPassword_hash())) {
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("None")
                .build();

        servletResponse.addHeader("Set-Cookie", cookie.toString());

        response.put("success", true);
        response.put("message", "Login successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(HttpServletResponse servletResponse) {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
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
}
