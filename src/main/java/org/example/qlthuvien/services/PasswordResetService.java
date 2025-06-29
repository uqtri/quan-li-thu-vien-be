package org.example.qlthuvien.services;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.entity.PasswordResetCode;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.repository.PasswordResetCodeRepository;
import org.example.qlthuvien.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetCodeRepository resetCodeRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;


    // Gửi mã xác nhận về email
    public void sendResetCode(String email) {


        // Xoá mã cũ nếu có
        resetCodeRepo.deleteByEmail(email);

        // Sinh mã OTP 6 chữ số
        String code = String.format("%06d", new Random().nextInt(999999));

        PasswordResetCode resetCode = new PasswordResetCode();
        resetCode.setEmail(email);
        resetCode.setCode(code);
        resetCode.setExpirationTime(LocalDateTime.now().plusMinutes(10));

        resetCodeRepo.save(resetCode);

        String subject = "Mã xác nhận đặt lại mật khẩu";
        String body = "Mã xác nhận của bạn là: " + code;
        emailService.send(email, subject, body);
    }

    // Đặt lại mật khẩu nếu mã xác nhận đúng
    public void resetPassword(String email, String code, String newPassword) {
        Optional<PasswordResetCode> optional = resetCodeRepo.findByEmailAndCode(email, code);
        if (optional.isEmpty() || optional.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã xác nhận không hợp lệ hoặc đã hết hạn");
        }

        User user = userRepo.findByEmail(email);
        user.setPassword_hash(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        userRepo.save(user);

        resetCodeRepo.delete(optional.get());
    }
}
