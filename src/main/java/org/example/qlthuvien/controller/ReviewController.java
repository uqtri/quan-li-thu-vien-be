package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.notification.NotificationResponse;
import org.example.qlthuvien.dto.review.ReviewResponse;
import org.example.qlthuvien.entity.Book;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.mapper.NotificationMapper;
import org.example.qlthuvien.mapper.ReviewMapper;
import org.example.qlthuvien.payload.ApiResponse;
import org.example.qlthuvien.repository.BookRepository;
import org.example.qlthuvien.repository.NotificationRepository;
import org.example.qlthuvien.repository.ReviewRepository;
import org.example.qlthuvien.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        List<ReviewResponse> userNotis = reviewRepository.findByUserIdOrderByCreatedAtDesc(book.getId())
                .stream()
                .map(reviewMapper::toResponse)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Xem nhận xét của sách thành công.", userNotis));
    }
}
