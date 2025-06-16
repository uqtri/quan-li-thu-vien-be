package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByUserId(Long userId, Pageable pageable);

    Page<Review> findByBookId(Long bookId, Pageable pageable);
    List<Review> findAllByOrderByCreatedAtDesc();
    boolean existsByUserIdAndBookId(Long userId, Long bookId);

}
