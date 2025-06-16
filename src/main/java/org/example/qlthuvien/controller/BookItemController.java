package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.bookitem.BookItemResponse;
import org.example.qlthuvien.mapper.BookItemMapper;
import org.example.qlthuvien.repository.BookItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book-items")
@RequiredArgsConstructor
public class BookItemController {

    private final BookItemRepository bookItemRepository;
    private final BookItemMapper bookItemMapper;

    @GetMapping

    public Page<BookItemResponse> getAllBookItems(Pageable pageable) {
        return bookItemRepository.findAll(pageable).map(bookItemMapper::toResponse);

    }

}
