package org.example.qlthuvien.controller;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.borrowedbook.BorrowedBookResponse;
import org.example.qlthuvien.dto.borrowedbook.CreateBorrowedBookRequest;
import org.example.qlthuvien.dto.borrowedbook.UpdateBorrowedBookRequest;
import org.example.qlthuvien.entity.BookItem;
import org.example.qlthuvien.entity.BorrowedBook;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.mapper.BorrowedBookMapper;
import org.example.qlthuvien.repository.BorrowedBookRepository;
import org.example.qlthuvien.services.BadgeService;
import org.example.qlthuvien.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/borrowed-books")
@RequiredArgsConstructor
public class BorrowedBookController {
    private final EntityManager entityManager;
    private final BorrowedBookRepository borrowedBookRepository;
    private final BorrowedBookMapper borrowedBookMapper;
    private final BadgeService badgeService;

    @GetMapping
    public List<BorrowedBookResponse> getAllBorrowedBooks() {
        return borrowedBookRepository.findAll()
                .stream()
                .map(borrowedBookMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public BorrowedBookResponse getBorrowedBookById(@PathVariable Long id) {
        BorrowedBook borrowedBook = borrowedBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BorrowedBook not found with id: " + id));
        return borrowedBookMapper.toResponse(borrowedBook);
    }

    private final UserService userService;
    @PostMapping
    public BorrowedBookResponse createBorrowedBook(@RequestBody CreateBorrowedBookRequest data) {

        User borrowUser = entityManager.find(User.class, data.getUser_id());
        BookItem bookItem = entityManager.find(BookItem.class, data.getBook_item_id());
        BorrowedBook entity = new BorrowedBook();
        entity.setUser(borrowUser);
        entity.setBook_item(bookItem);
        BorrowedBook saved = borrowedBookRepository.save(entity);

        userService.addXp(borrowUser, 5);
        badgeService.checkBadges(borrowUser.getId());
        return borrowedBookMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    public BorrowedBookResponse updateBorrowedBook(@PathVariable Long id, @RequestBody UpdateBorrowedBookRequest data) {
        BorrowedBook existing = borrowedBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BorrowedBook not found with ID: " + id));

        existing = borrowedBookMapper.updateEntity(existing, borrowedBookMapper.toEntity(data));
        BorrowedBook updated = borrowedBookRepository.save(existing);
        return borrowedBookMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBorrowedBook(@PathVariable Long id) {
        Map <String, Object> response = new HashMap<>();
        try {
            borrowedBookRepository.deleteById(id);
            response.put("message", "Borrowed book successfully deleted");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
