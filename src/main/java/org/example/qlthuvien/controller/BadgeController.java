package org.example.qlthuvien.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.example.qlthuvien.dto.badge.BadgeResponse;
import org.example.qlthuvien.dto.badge.CreateBadgeRequest;
import org.example.qlthuvien.entity.Badge;
import org.example.qlthuvien.mapper.BadgeMapper;
import org.example.qlthuvien.payload.ApiResponse;
import org.example.qlthuvien.repository.BadgeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/badges")
@AllArgsConstructor
public class BadgeController {
    private final BadgeRepository  badgeRepository;
    private final BadgeMapper badgeMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<BadgeResponse>> createReview(@RequestBody CreateBadgeRequest request) {

        Badge badge = Badge
                .builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .borrowedBooksRequired(request.getBorrowedBooksRequired())
                .reviewsRequired(request.getReviewsRequired())
                .xpRequired(request.getXpRequired())
                .icon_url(request.getIcon_url())
                .xpAwarded(request.getXpAwarded())
                .build();

        Badge saved = badgeRepository.save(badge);
        ApiResponse<BadgeResponse> response = new ApiResponse<>(true, "Huy hiệu đã được tạo.", badgeMapper.toResponse(saved));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
