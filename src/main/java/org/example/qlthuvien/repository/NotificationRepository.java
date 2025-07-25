package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findAllByOrderByCreatedAtDesc();
    List<Notification> findByUserIdAndSeenOrderByCreatedAtDesc(Long userId, Boolean seen);
    List<Notification> findBySeenOrderByCreatedAtDesc(Boolean seen);


}
