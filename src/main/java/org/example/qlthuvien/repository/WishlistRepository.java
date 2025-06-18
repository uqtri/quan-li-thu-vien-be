package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    List<Wishlist> findByBookId(Long bookId);
    boolean existsByUserAndBook(User user, Book book);
    void deleteByUserAndBook(User user, Book book);
}
