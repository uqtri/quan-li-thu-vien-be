package org.example.qlthuvien.dto.borrowedbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBorrowedBookRequest {
    private Long user_id;
    private Long book_item_id;
}
