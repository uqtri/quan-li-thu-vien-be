package org.example.qlthuvien.controller;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.book.CreateBookRequest;
import org.example.qlthuvien.dto.bookitem.BookItemResponse;
import org.example.qlthuvien.dto.bookitem.CreateBookItemRequest;
import org.example.qlthuvien.dto.bookitem.UpdateBookItemRequest;
import org.example.qlthuvien.entity.Book;
import org.example.qlthuvien.entity.BookItem;
import org.example.qlthuvien.mapper.BookItemMapper;
import org.example.qlthuvien.repository.BookItemRepository;
import org.example.qlthuvien.repository.BookRepository;
import org.example.qlthuvien.repository.ReservationRepository;
import org.example.qlthuvien.services.EmailService;
import org.example.qlthuvien.utils.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/book-items")
@RequiredArgsConstructor
public class BookItemController {

    private final BookItemRepository bookItemRepository;
    private final BookItemMapper bookItemMapper;
    private final ReservationRepository reservationRepository;
    private final EntityManager entityManager;
    private final EmailService emailService;
    private final BookRepository bookRepository;
    private final JwtUtil jwtUtil;
    @GetMapping

    public Page<BookItemResponse> getAllBookItems(Pageable pageable) {
        return bookItemRepository.findAll(pageable).map(bookItemMapper::toResponse);
    }
    @PostMapping
    public BookItemResponse createBookItem(@RequestBody CreateBookItemRequest data,  @CookieValue(name = "jwt", required = false) String token) {
        BookItem bookItem = bookItemMapper.toEntity(data);
        System.out.println(data.getBook_id());
        Book book = entityManager.find(Book.class, data.getBook_id());
        bookItem.setBook(book);

        Long bookId = bookItem.getBook().getId();
        reservationRepository.updateReturnedByBookItemBookId(bookId);
        String htmlContent = emailService.loadEmailTemplate("emailTemplate.html").replace("{bookTitle}", bookItem.getBook().getTitle());
        System.out.println(htmlContent);

        String email = getEmailFromToken(token);
        emailService.sendHtmlEmail(email, "Thông báo đặt sách thành công", htmlContent);
        return bookItemMapper.toResponse(bookItemRepository.save(bookItem));
     }
    @PutMapping("/{id}")
    public BookItemResponse updateBookItem(@PathVariable Long id, @RequestBody UpdateBookItemRequest data) {
        BookItem bookItem = bookItemMapper.toEntity(data);
        BookItem existedBookItem = bookItemRepository.findById(id).orElse(null);
        existedBookItem = bookItemMapper.updateEntity(existedBookItem, bookItem);

        if ("AVAILABLE".equalsIgnoreCase(String.valueOf(existedBookItem.getStatus()))) {
            Long bookId = existedBookItem.getBook().getId();
            reservationRepository.updateReturnedByBookItemBookId(bookId);
        } else {
            Long bookId = existedBookItem.getBook().getId();
            long availableCount = bookItemRepository.countAvailableByBookId(bookId);
            System.out.println(availableCount);
            if (availableCount - 1 == 0) {
                reservationRepository.updateReturnedFalseByBookItemBookId(bookId); // thêm cái này
            }
        }

        return bookItemMapper.toResponse(bookItemRepository.save(existedBookItem));
    }
    @GetMapping("/book/{id}")
    public List<BookItemResponse> getAllBookItemsByBookId(@PathVariable Long id) {
        return bookItemRepository.findBookItemsByBookId(id).stream().map(bookItemMapper::toResponse).toList();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookItem(@PathVariable Long id) {
        try {
            BookItem bookItem = bookItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("BookItem not found"));

            Long bookId = bookItem.getBook().getId();

            bookItemRepository.deleteById(id);

            long availableCount = bookItemRepository.countAvailableByBookId(bookId);
            if (availableCount == 0) {
                reservationRepository.updateReturnedFalseByBookItemBookId(bookId);
            }

            return ResponseEntity.ok("Book item deleted");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    private String getEmailFromToken(String token) {
        return token != null && jwtUtil.validateToken(token) ? jwtUtil.extractEmail(token) : null;
    }
}
