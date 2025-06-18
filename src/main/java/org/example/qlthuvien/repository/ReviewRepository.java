package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByUserId(Long userId, Pageable pageable);

    Page<Review> findByBookId(Long bookId, Pageable pageable);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
    Page<Review> findByRating(Float rating, Pageable pageable);
    Page<Review> findByUserIdAndRating(Long userId, Float rating, Pageable pageable);
    Page<Review> findByBookIdAndRating(Long bookId, Float rating, Pageable pageable);
    int countByUserId(Long userId);

}
