package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.example.qlthuvien.services.EmailService;
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestParam String to,
                                       @RequestParam String subject,
                                       @RequestParam String body) {
        emailService.sendSimpleEmail(to, subject, body);
        return ResponseEntity.ok("Email sent successfully!");
    }
}
