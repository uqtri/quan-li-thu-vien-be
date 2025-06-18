package org.example.qlthuvien.services;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.entity.Notification;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void sendNotification(User user, String message) {
        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }
}
