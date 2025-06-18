package org.example.qlthuvien.repository;

import org.example.qlthuvien.dto.badge.BadgeWithUserResponse;
import org.example.qlthuvien.entity.UserBadge;
import org.example.qlthuvien.entity.UserBadgeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, UserBadgeId> {

    @Query("SELECT ub.id.badgeId FROM UserBadge ub WHERE ub.id.userId = :userId")
    Set<Long> findBadgeIdsByUserId(Long userId);
    List<UserBadge> findByUserId(Long userId);
    List<UserBadge> findByBadgeId(Long badgeId);
    @Query("SELECT ub FROM UserBadge ub WHERE ub.user.id = :userId AND ub.badge.isDeleted = false")
    List<UserBadge> findByUserIdAndBadgeDeletedFalse(@Param("userId") Long userId);



}
