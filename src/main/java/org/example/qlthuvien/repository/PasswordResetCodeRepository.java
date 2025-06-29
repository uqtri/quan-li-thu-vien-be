package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findByEmailAndCode(String email, String code);
    void deleteByEmail(String email); // để xoá code cũ trước khi gửi mã mới (nếu muốn)
}
