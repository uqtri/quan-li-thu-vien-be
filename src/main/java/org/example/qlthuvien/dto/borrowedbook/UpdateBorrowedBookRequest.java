package org.example.qlthuvien.dto.borrowedbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.entity.LendingStatus;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBorrowedBookRequest {
    private LendingStatus status;
    private LocalDateTime return_date;
}
