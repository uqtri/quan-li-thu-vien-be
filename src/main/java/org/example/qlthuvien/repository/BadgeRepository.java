package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Optional<Badge> findByName(String name);
}
