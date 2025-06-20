package org.example.qlthuvien.services;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.repository.BookRepository;
import org.example.qlthuvien.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    public Double calculateRating(Long bookId) {
        Double averageRating = reviewRepository.calculateAverageRatingByBookId(bookId);
        return averageRating != null ? averageRating : 0.0;
    }
}

