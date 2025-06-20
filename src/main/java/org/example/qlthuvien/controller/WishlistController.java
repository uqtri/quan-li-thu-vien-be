package org.example.qlthuvien.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.entity.*;
import org.example.qlthuvien.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    // Thêm sách vào wishlist
    @PostMapping("/{userId}/{bookId}")
    public ResponseEntity<?> addToWishlist(@PathVariable Long userId, @PathVariable Long bookId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);

        if (userOpt.isEmpty() || bookOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User hoặc Book không tồn tại");
        }

        // Kiểm tra xem đã có trong wishlist chưa
        if (wishlistRepository.existsByUserAndBook(userOpt.get(), bookOpt.get())) {
            return ResponseEntity.ok("Đã có trong wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(userOpt.get());
        wishlist.setBook(bookOpt.get());

        wishlistRepository.save(wishlist);

        return ResponseEntity.ok("Thêm vào wishlist thành công");
    }

    @DeleteMapping("/{userId}/{bookId}")
    @Transactional
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long userId, @PathVariable Long bookId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);

        if (userOpt.isEmpty() || bookOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User hoặc Book không tồn tại");
        }

        wishlistRepository.deleteByUserAndBook(userOpt.get(), bookOpt.get());

        return ResponseEntity.ok("Đã xoá khỏi wishlist");
    }

    // Lấy danh sách wishlist theo user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getWishlistByUser(@PathVariable Long userId) {
        List<Wishlist> wishlist = wishlistRepository.findByUserId(userId);
        return ResponseEntity.ok(wishlist);
    }

    // Lấy danh sách user đã wishlist cuốn sách
    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getUsersWhoWishedBook(@PathVariable Long bookId) {
        List<Wishlist> wishlisted = wishlistRepository.findByBookId(bookId);
        return ResponseEntity.ok(wishlisted);
    }
}
