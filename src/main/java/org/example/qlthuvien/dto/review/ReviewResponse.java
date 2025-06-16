package org.example.qlthuvien.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.dto.book.BookResponse;
import org.example.qlthuvien.dto.user.UserResponse;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Long review_id;
    private UserResponse user;
    private BookResponse book;
    private Float rating;
    private String comment;
    private LocalDateTime createdAt;
}
