package org.example.qlthuvien.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.dto.user.UserResponse;
import org.example.qlthuvien.entity.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private UserResponse user;
    private boolean seen;
    private String message;
    private LocalDateTime createdAt;
}
