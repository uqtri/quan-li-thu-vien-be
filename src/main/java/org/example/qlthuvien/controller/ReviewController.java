package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;

import org.example.qlthuvien.dto.review.CreateReviewRequest;
import org.example.qlthuvien.dto.review.DeleteReviewsRequest;
import org.example.qlthuvien.dto.review.ReviewResponse;
import org.example.qlthuvien.dto.review.UpdateReviewRequest;
import org.example.qlthuvien.entity.Book;
import org.example.qlthuvien.entity.Review;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.mapper.ReviewMapper;
import org.example.qlthuvien.payload.ApiResponse;
import org.example.qlthuvien.repository.BookRepository;
import org.example.qlthuvien.repository.ReviewRepository;
import org.example.qlthuvien.repository.UserRepository;
import org.example.qlthuvien.services.BadgeService;
import org.example.qlthuvien.services.ReviewService;
import org.example.qlthuvien.services.UserService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class                     ReviewController {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper ;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final BadgeService badgeService;
    private final ReviewService reviewService;


    @GetMapping
    public ResponseEntity<?> getAllReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Float rating
    ) {
        if (page < 1) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Số trang phải ≥ 1", null)
            );
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Review> reviewPage;

        if (rating != null) {
            reviewPage = reviewRepository.findByRating(rating, pageable);
        } else {
            reviewPage = reviewRepository.findAll(pageable);
        }

        List<ReviewResponse> reviewResponses = reviewPage.getContent()
                .stream()
                .map(reviewMapper::toResponse)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("items", reviewResponses);
        response.put("currentPage", reviewPage.getNumber() + 1);
        response.put("totalItems", reviewPage.getTotalElements());
        response.put("totalPages", reviewPage.getTotalPages());

        return ResponseEntity.ok(new ApiResponse<>(true, "Xem nhận xét thành công.", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhận xét."));

        ReviewResponse response = reviewMapper.toResponse(review);

        return ResponseEntity.ok(new ApiResponse<>(true, "Xem chi tiết nhận xét thành công.", response));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Float rating
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        Page<Review> reviewPage = (rating != null)
                ? reviewRepository.findByUserIdAndRating(userId, rating, pageable)
                : reviewRepository.findByUserId(userId, pageable);

        List<ReviewResponse> reviewResponses = reviewPage.getContent()
                .stream()
                .map(reviewMapper::toResponse)
                .toList();

        ApiResponse<?> response = new ApiResponse<>(
                true,
                "Xem nhận xét của người dùng thành công.",
                Map.of(
                        "items", reviewResponses,
                        "currentPage", reviewPage.getNumber() + 1,
                        "totalPages", reviewPage.getTotalPages(),
                        "totalItems", reviewPage.getTotalElements()
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getReviewsByBookId(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Float rating
    ) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách."));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        Page<Review> reviewPage = (rating != null)
                ? reviewRepository.findByBookIdAndRating(bookId, rating, pageable)
                : reviewRepository.findByBookId(bookId, pageable);

        List<ReviewResponse> reviewResponses = reviewPage.getContent()
                .stream()
                .map(reviewMapper::toResponse)
                .toList();

        ApiResponse<?> response = new ApiResponse<>(
                true,
                "Xem nhận xét của sách thành công.",
                Map.of(
                        "items", reviewResponses,
                        "currentPage", reviewPage.getNumber() + 1,
                        "totalPages", reviewPage.getTotalPages(),
                        "totalItems", reviewPage.getTotalElements()
                )
        );

        return ResponseEntity.ok(response);
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

//        Review saved = reviewRepository.save(review);
        reviewService.addReview(review);

        userService.addXp(user, 1);
        badgeService.checkBadges(userId);

        ApiResponse<ReviewResponse> response = new ApiResponse<>(true, "Nhận xét đã được tạo.", reviewMapper.toResponse(review));
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

    @DeleteMapping
    public ResponseEntity<?> deleteReviews(@RequestBody DeleteReviewsRequest request) {
        reviewRepository.deleteAllById(request.getIds());

        return ResponseEntity.ok(new ApiResponse<>(true,
                "Đã xóa " + request.getIds().size() + " nhận xét.",
                null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhận xét."));
        userService.addXp(review.getUser(), -1);
        badgeService.revokeInvalidBadges(review.getUser().getId());
        reviewRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Nhận xét được xóa thành công.", null));
    }
}
