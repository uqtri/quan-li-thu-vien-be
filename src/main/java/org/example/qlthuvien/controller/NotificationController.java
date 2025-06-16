package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.notification.CreateNotificationRequest;
import org.example.qlthuvien.dto.notification.DeleteNotificationsRequest;
import org.example.qlthuvien.dto.notification.NotificationResponse;
import org.example.qlthuvien.dto.notification.UpdateNotificationRequest;
import org.example.qlthuvien.entity.Notification;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.mapper.NotificationMapper;
import org.example.qlthuvien.repository.NotificationRepository;
import org.example.qlthuvien.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @GetMapping
    public List<NotificationResponse> getAllNotifications() {

        return notificationRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @GetMapping("/{userId}")
    public List<NotificationResponse> getNotificationsByUserId(@PathVariable Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }


    @PostMapping
    public NotificationResponse createNotification(@RequestBody CreateNotificationRequest request) {
        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .user(user)
                .message(request.getMessage())
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();

        Notification saved = notificationRepository.save(notification);
        return notificationMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    public NotificationResponse updateNotification(@PathVariable Long id, @RequestBody UpdateNotificationRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setSeen(request.isSeen());
        notification.setMessage(request.getMessage());

        Notification updated = notificationRepository.save(notification);
        return notificationMapper.toResponse(updated);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteNotifications(@RequestBody DeleteNotificationsRequest deleteNotificationsRequest) {
        notificationRepository.deleteAllById(deleteNotificationsRequest.getIds());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("deletedCount", deleteNotificationsRequest.getIds().size());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        boolean exists = notificationRepository.existsById(id);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", "Notification not found!"
                    ));
        }

        notificationRepository.deleteById(id);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Notification deleted successfully!"
        ));
    }


}
