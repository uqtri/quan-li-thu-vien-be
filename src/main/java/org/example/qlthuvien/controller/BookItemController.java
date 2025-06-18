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

    private final EntityManager entityManager;

    private final BookRepository bookRepository;
    @GetMapping

    public Page<BookItemResponse> getAllBookItems(Pageable pageable) {
        return bookItemRepository.findAll(pageable).map(bookItemMapper::toResponse);
    }
    @PostMapping
    public BookItemResponse createBookItem(@RequestBody CreateBookItemRequest data) {
        BookItem bookItem = bookItemMapper.toEntity(data);
        System.out.println(data.getBook_id());
        Book book = entityManager.find(Book.class, data.getBook_id());
        bookItem.setBook(book);

        return bookItemMapper.toResponse(bookItemRepository.save(bookItem));
     }
    @PutMapping("/{id}")
    public BookItemResponse updateBookItem(@PathVariable Long id, @RequestBody UpdateBookItemRequest data) {
        BookItem bookItem = bookItemMapper.toEntity(data);
        BookItem existedBookItem = bookItemRepository.findById(id).orElse(null);
        existedBookItem = bookItemMapper.updateEntity(existedBookItem, bookItem);

        return bookItemMapper.toResponse(bookItemRepository.save(existedBookItem));
    }
    @GetMapping("/book/{id}")
    public List<BookItemResponse> getAllBookItemsByBookId(@PathVariable Long id) {
        return bookItemRepository.findBookItemsByBookId(id).stream().map(bookItemMapper::toResponse).toList();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookItem(@PathVariable Long id) {
        try {
            bookItemRepository.deleteById(id);
            return ResponseEntity.ok("Book item deleted");
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
