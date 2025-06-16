package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.Notification;
import org.example.qlthuvien.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Review> findByBookIdOrderByCreatedAtDesc(Long bookId);
    List<Review> findAllByOrderByCreatedAtDesc();

}
