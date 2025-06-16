package org.example.qlthuvien.dto.borrowedbook;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.entity.BookItem;
import org.example.qlthuvien.entity.LendingStatus;
import org.example.qlthuvien.entity.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBookResponse {
    private Long id;
    private BookItem book_item;
    private User user;
    private LocalDateTime borrow_date;
    private LocalDateTime return_date;
    private LendingStatus status;
}
