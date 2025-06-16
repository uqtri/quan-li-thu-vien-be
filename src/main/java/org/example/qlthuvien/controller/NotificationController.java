package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.notification.CreateNotificationRequest;
import org.example.qlthuvien.dto.notification.DeleteNotificationsRequest;
import org.example.qlthuvien.dto.notification.NotificationResponse;
import org.example.qlthuvien.dto.notification.UpdateNotificationRequest;
import org.example.qlthuvien.entity.Notification;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.mapper.NotificationMapper;
import org.example.qlthuvien.payload.ApiResponse;
import org.example.qlthuvien.repository.NotificationRepository;
import org.example.qlthuvien.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getAllNotifications(@RequestParam(required = false) Boolean seen) {
        List<Notification> notis = (seen != null)
                ? notificationRepository.findBySeenOrderByCreatedAtDesc(seen)
                : notificationRepository.findAllByOrderByCreatedAtDesc();

        List<NotificationResponse> notifications = notis.stream()
                .map(notificationMapper::toResponse)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Xem thông báo thành công.", notifications));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotificationsByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean seen) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        List<Notification> notifications;

        if (seen != null) {
            notifications = notificationRepository.findByUserIdAndSeenOrderByCreatedAtDesc(userId, seen);
        } else {
            notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }

        List<NotificationResponse> userNotis = notifications.stream()
                .map(notificationMapper::toResponse)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Xem thông báo người dùng thành công", userNotis));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(@RequestBody CreateNotificationRequest request) {
        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        Notification notification = Notification.builder()
                .user(user)
                .message(request.getMessage())
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();

        Notification saved = notificationRepository.save(notification);
        ApiResponse<NotificationResponse> response = new ApiResponse<>(true, "Thông báo đã được tạo.", notificationMapper.toResponse(saved));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotification(@PathVariable Long id, @RequestBody UpdateNotificationRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo."));

        if (request.getSeen() != null) notification.setSeen(request.getSeen());
        if (request.getMessage() !=  null) notification.setMessage(request.getMessage());

        Notification updated = notificationRepository.save(notification);
        return ResponseEntity.ok(new ApiResponse<>(true, "Thông báo được cập nhật thành công.", notificationMapper.toResponse(updated)));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteNotifications(@RequestBody DeleteNotificationsRequest request) {
        notificationRepository.deleteAllById(request.getIds());

        return ResponseEntity.ok(new ApiResponse<>(true,
                "Đã xóa " + request.getIds().size() + " thông báo.",
                null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        if (!notificationRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Không tìm thấy thông báo.", null));
        }

        notificationRepository.deleteById(id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Thông báo được xóa thành công.", null));
    }
}
