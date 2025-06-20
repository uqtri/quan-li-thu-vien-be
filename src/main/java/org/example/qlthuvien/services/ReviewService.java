package org.example.qlthuvien.services;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.entity.Review;
import org.example.qlthuvien.repository.BookRepository;
import org.example.qlthuvien.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookService bookService;
    private final BookRepository bookRepository;

    public Review addReview(Review review) {
        Review savedReview = reviewRepository.save(review);

        Double newAverage = bookService.calculateRating(review.getBook().getId());

        bookRepository.findById(review.getBook().getId()).ifPresent(book -> {
            book.setAvg_rating(newAverage);
            bookRepository.save(book);
        });

        return savedReview;
    }
}
