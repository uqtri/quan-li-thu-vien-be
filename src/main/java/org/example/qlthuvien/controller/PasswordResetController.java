package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.auth.ResetPasswordWithCodeRequest;
import org.example.qlthuvien.services.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/send-reset-code")
    public ResponseEntity<?> sendResetCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        passwordResetService.sendResetCode(email);
        return ResponseEntity.ok("Đã gửi mã xác nhận về email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordWithCodeRequest request) {
        passwordResetService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        return ResponseEntity.ok("Đặt lại mật khẩu thành công");
    }
}
