package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.notification.CreateNotificationRequest;
import org.example.qlthuvien.dto.notification.NotificationResponse;
import org.example.qlthuvien.dto.notification.UpdateNotificationRequest;
import org.example.qlthuvien.dto.review.CreateReviewRequest;
import org.example.qlthuvien.dto.review.ReviewResponse;
import org.example.qlthuvien.dto.review.UpdateReviewRequest;
import org.example.qlthuvien.entity.Book;
import org.example.qlthuvien.entity.Notification;
import org.example.qlthuvien.entity.Review;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.mapper.ReviewMapper;
import org.example.qlthuvien.payload.ApiResponse;
import org.example.qlthuvien.repository.BookRepository;
import org.example.qlthuvien.repository.ReviewRepository;
import org.example.qlthuvien.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper ;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @GetMapping
    public ResponseEntity<?> getAllNotifications() {
        List<ReviewResponse> reviews = reviewRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(reviewMapper::toResponse)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Xem nhận xét thành công.", reviews));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUserId(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        List<ReviewResponse> userNotis = reviewRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(reviewMapper::toResponse)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Xem nhận xét của người dùng thành công.", userNotis));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getReviewsByBookId(@PathVariable Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        List<ReviewResponse> userNotis = reviewRepository.findByBookIdOrderByCreatedAtDesc(book.getId())
                .stream()
                .map(reviewMapper::toResponse)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Xem nhận xét của sách thành công.", userNotis));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(@RequestBody CreateReviewRequest request) {
        Long userId = request.getUser_id();
        Long bookId = request.getBook_id();

        if (reviewRepository.existsByUserIdAndBookId(userId, bookId)) {
            ApiResponse<ReviewResponse> errorResponse = new ApiResponse<>(
                    false,
                    "Bạn đã đánh giá sách này rồi.",
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách."));

        Review review = Review.builder()
                .user(user)
                .book(book)
                .comment(request.getComment())
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .build();

        Review saved = reviewRepository.save(review);
        ApiResponse<ReviewResponse> response = new ApiResponse<>(true, "Nhận xét đã được tạo.", reviewMapper.toResponse(saved));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody UpdateReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhận xét."));

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }

        if (request.getComment() != null && !request.getComment().trim().isEmpty()) {
            review.setComment(request.getComment());
        }

        Review updated = reviewRepository.save(review);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Nhận xét được cập nhật thành công.", reviewMapper.toResponse(updated))
        );
    }
}
