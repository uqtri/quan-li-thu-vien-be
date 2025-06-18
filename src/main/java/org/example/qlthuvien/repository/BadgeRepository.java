package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.Badge;
import org.example.qlthuvien.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findAllByIsDeletedFalse();
    Optional<Badge> findByName(String name);
}
