package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.UserBadge;
import org.example.qlthuvien.entity.UserBadgeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserBadgeRepository extends JpaRepository<UserBadge, UserBadgeId> {

    @Query("SELECT ub.id.badgeId FROM UserBadge ub WHERE ub.id.userId = :userId")
    Set<Long> findBadgeIdsByUserId(Long userId);
}
