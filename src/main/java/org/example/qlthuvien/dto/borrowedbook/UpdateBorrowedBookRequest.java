package org.example.qlthuvien.dto.borrowedbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.entity.LendingStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBorrowedBookRequest {
    private LendingStatus status;
}
