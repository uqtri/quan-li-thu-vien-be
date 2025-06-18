package org.example.qlthuvien.services;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.entity.Badge;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.entity.UserBadge;
import org.example.qlthuvien.repository.*;
import org.example.qlthuvien.services.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepo;
    private final UserRepository userRepo;
    private final BorrowedBookRepository borrowRepo;
    private final ReviewRepository reviewRepo;
    private final UserBadgeRepository userBadgeRepo;
    private final NotificationService notificationService;
    private final UserService userService;

    public void checkBadges(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        Set<Long> ownedBadgeIds = userBadgeRepo.findBadgeIdsByUserId(userId);
        List<Badge> allBadges = badgeRepo.findAll();

        int reviewCount = reviewRepo.countByUserId(userId);
        int borrowCount = borrowRepo.countByUserId(userId);
        int xp = user.getXp() == null ? 0 : user.getXp();

        for (Badge badge : allBadges) {
            if (ownedBadgeIds.contains(badge.getId())) continue;

            if (isBadgeUnlocked(badge, borrowCount, reviewCount, xp)) {
                userBadgeRepo.save(new UserBadge(null, user, badge, LocalDateTime.now()));
                userService.addXp(user, badge.getXpAwarded());
                notificationService.sendNotification(user, "Bạn đã nhận huy hiệu: " + badge.getName() + " 🎉 (+ " + badge.getXpAwarded() + " XP)");
            }
        }
    }

    private boolean isBadgeUnlocked(Badge badge, int borrowCount, int reviewCount, int xp) {
        return (badge.getBorrowedBooksRequired() == null || borrowCount >= badge.getBorrowedBooksRequired())
                && (badge.getReviewsRequired() == null || reviewCount >= badge.getReviewsRequired())
                && (badge.getXpRequired() == null || xp >= badge.getXpRequired());
    }
}
