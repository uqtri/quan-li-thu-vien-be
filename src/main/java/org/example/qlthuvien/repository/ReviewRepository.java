package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByUserId(Long userId, Pageable pageable);

    Page<Review> findByBookId(Long bookId, Pageable pageable);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
    Page<Review> findByRating(Float rating, Pageable pageable);
    Page<Review> findByUserIdAndRating(Long userId, Float rating, Pageable pageable);
    Page<Review> findByBookIdAndRating(Long bookId, Float rating, Pageable pageable);
    int countByUserId(Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double calculateAverageRatingByBookId(@Param("bookId") Long bookId);

}
