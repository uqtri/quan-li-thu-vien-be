package org.example.qlthuvien.controller;

import lombok.AllArgsConstructor;

import org.example.qlthuvien.dto.badge.BadgeResponse;
import org.example.qlthuvien.dto.badge.CreateBadgeRequest;

import org.example.qlthuvien.dto.badge.UserBadgeResponse;
import org.example.qlthuvien.entity.Badge;

import org.example.qlthuvien.entity.UserBadge;
import org.example.qlthuvien.mapper.BadgeMapper;
import org.example.qlthuvien.mapper.UserBadgeMapper;
import org.example.qlthuvien.payload.ApiResponse;
import org.example.qlthuvien.repository.BadgeRepository;
import org.example.qlthuvien.repository.UserBadgeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@AllArgsConstructor
public class BadgeController {
    private final BadgeRepository badgeRepository;
    private final BadgeMapper badgeMapper;
    private final UserBadgeRepository userBadgeRepository;
    private final UserBadgeMapper userBadgeMapper;

    @GetMapping
    public ResponseEntity<?> getAllBadges() {
        List<Badge> badges = badgeRepository.findAll();

        List<BadgeResponse> badgeList = badges.stream()
                .map(badgeMapper::toResponse)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Xem huy hiệu thành công.", badgeList));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBadges(@PathVariable Long userId) {
        List<UserBadge> badges = userBadgeRepository.findByUserId(userId);

        List<UserBadgeResponse> response = badges.stream()
                .map(userBadgeMapper::toResponse)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy huy hiệu của người dùng thành công.", response));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<BadgeResponse>> createBadge(@RequestBody CreateBadgeRequest request) {

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
