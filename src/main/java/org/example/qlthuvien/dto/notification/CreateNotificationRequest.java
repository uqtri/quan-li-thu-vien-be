package org.example.qlthuvien.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.entity.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest
{
    private String user_id;
    private boolean seen;
    private String message;
    private LocalDateTime created_at;
}
