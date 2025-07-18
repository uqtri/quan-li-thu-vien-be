package org.example.qlthuvien.controller;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.bookitem.STATUS;
import org.example.qlthuvien.dto.bookitem.UpdateBookItemRequest;
import org.example.qlthuvien.dto.borrowedbook.BorrowedBookResponse;
import org.example.qlthuvien.dto.borrowedbook.CreateBorrowedBookRequest;
import org.example.qlthuvien.dto.borrowedbook.UpdateBorrowedBookRequest;
import org.example.qlthuvien.entity.BookItem;
import org.example.qlthuvien.entity.BorrowedBook;
import org.example.qlthuvien.entity.LendingStatus;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.mapper.BorrowedBookMapper;
import org.example.qlthuvien.repository.BookItemRepository;
import org.example.qlthuvien.repository.BorrowedBookRepository;
import org.example.qlthuvien.repository.ReservationRepository;
import org.example.qlthuvien.services.BadgeService;
import org.example.qlthuvien.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/borrowed-books")
@RequiredArgsConstructor
public class BorrowedBookController {
    private final EntityManager entityManager;
    private final BorrowedBookRepository borrowedBookRepository;
    private final BookItemRepository bookItemRepository;
    private final BorrowedBookMapper borrowedBookMapper;
    private final BadgeService badgeService;
    private final BookItemController bookItemController;
    private final ReservationRepository reservationRepository;
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


        UpdateBookItemRequest updateBookItemRequest = new UpdateBookItemRequest();
        updateBookItemRequest.setStatus(STATUS.Borrowed);
        bookItemController.updateBookItem(bookItem.getId(), updateBookItemRequest);

        reservationRepository.deleteByBookIdAndUserId(bookItem.getBook().getId(), borrowUser.getId());
        return borrowedBookMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    public BorrowedBookResponse updateBorrowedBook(@PathVariable Long id, @RequestBody UpdateBorrowedBookRequest data) {
        BorrowedBook existing = borrowedBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BorrowedBook not found with ID: " + id));

        existing = borrowedBookMapper.updateEntity(existing, borrowedBookMapper.toEntity(data));

        BorrowedBook temp = borrowedBookMapper.toEntity(data);
        BorrowedBook updated = borrowedBookRepository.save(existing);
        BookItem bookItem = existing.getBook_item();
        UpdateBookItemRequest updateBookItemRequest = new UpdateBookItemRequest();
        if (updated.getStatus() == LendingStatus.BORROWED) {
            updateBookItemRequest.setStatus(STATUS.Borrowed);
            User borrowUser = updated.getUser();
            userService.addXp(borrowUser, 5);
            badgeService.checkBadges(borrowUser.getId());
        }
        if (updated.getStatus() == LendingStatus.PENDING) {
            updateBookItemRequest.setStatus(STATUS.Borrowed);

        }
        if (updated.getStatus() == LendingStatus.RETURNED) {
            updateBookItemRequest.setStatus(STATUS.AVAILABLE);
        }
        bookItemController.updateBookItem(bookItem.getId(), updateBookItemRequest);
        return borrowedBookMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBorrowedBook(@PathVariable Long id) {
        Map <String, Object> response = new HashMap<>();
        try {
            BorrowedBook borrowedBook = borrowedBookRepository.findById(id).orElseThrow();
            BookItem item = borrowedBook.getBook_item();
            if (item != null) {
                item.setBorrowedBook(null); // cắt mối quan hệ
                UpdateBookItemRequest updateBookItemRequest = new UpdateBookItemRequest();
                updateBookItemRequest.setStatus(STATUS.AVAILABLE);
                bookItemController.updateBookItem(item.getId(), updateBookItemRequest);
            }
            borrowedBookRepository.deleteById(id);
            response.put("message", "Borrowed book successfully deleted");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
