package org.example.qlthuvien.controller;

import lombok.AllArgsConstructor;

import org.example.qlthuvien.dto.badge.*;

import org.example.qlthuvien.dto.review.DeleteReviewsRequest;
import org.example.qlthuvien.dto.review.UpdateReviewRequest;
import org.example.qlthuvien.entity.Badge;

import org.example.qlthuvien.entity.Review;
import org.example.qlthuvien.entity.UserBadge;
import org.example.qlthuvien.mapper.BadgeMapper;
import org.example.qlthuvien.mapper.BadgeOwnerMapper;
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
    private final BadgeOwnerMapper badgeOwnerMapper;

    @GetMapping
    public ResponseEntity<?> getAllBadgesWithUsers() {
        List<Badge> badges = badgeRepository.findAllByIsDeletedFalse();

        List<BadgeWithUserResponse> responseList = badges.stream()
                .map(badge -> {
                    List<UserBadge> userBadges = userBadgeRepository.findByBadgeId(badge.getId());
                    List<BadgeOwner> owners = userBadges.stream().map(badgeOwnerMapper::toResponse)
                            .toList();

                    return new BadgeWithUserResponse(badgeMapper.toResponse(badge), owners);
                })
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Xem tất cả huy hiệu và người dùng đã nhận.", responseList)
        );
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBadges(@PathVariable Long userId) {
        List<UserBadge> badges = userBadgeRepository.findByUserIdAndBadgeDeletedFalse(userId);

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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody UpdateBadgeRequest request) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy huy hiệu."));

        if (request.getName() != null) {
            badge.setName(request.getName());
        }
        if (request.getCategory() != null) {
            badge.setCategory(request.getCategory());
        }
        if (request.getDescription() != null) {
            badge.setDescription(request.getDescription());
        }
        if (request.getIcon_url() != null) {
            badge.setIcon_url(request.getIcon_url());
        }
        if (request.getXpAwarded() != null) {
            badge.setXpAwarded(request.getXpAwarded());
        }
        if (request.getXpRequired() != null) {
            badge.setXpRequired(request.getXpRequired());
        }
        if (request.getReviewsRequired() != null) {
            badge.setReviewsRequired(request.getReviewsRequired());
        }
        if (request.getBorrowedBooksRequired() != null) {
            badge.setBorrowedBooksRequired(request.getBorrowedBooksRequired());
        }

        Badge updated = badgeRepository.save(badge);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Huy hiệu được cập nhật thành công.", badgeMapper.toResponse(updated))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy huy hiệu."));
        badge.setIsDeleted(true);
        badgeRepository.save(badge);
        return ResponseEntity.ok(new ApiResponse<>(true, "Huy hiệu được xóa thành công.", null));
    }
}
