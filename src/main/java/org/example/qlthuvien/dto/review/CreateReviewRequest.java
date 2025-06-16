package org.example.qlthuvien.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.entity.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest
{
    private Long book_id;
    private Long user_id;
    private String comment;
    private Float rating;
}
