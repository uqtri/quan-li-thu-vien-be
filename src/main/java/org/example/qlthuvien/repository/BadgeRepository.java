package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Optional<Badge> findByName(String name);
}
