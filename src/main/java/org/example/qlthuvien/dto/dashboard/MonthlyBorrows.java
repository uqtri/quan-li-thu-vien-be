package org.example.qlthuvien.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyBorrows {
    private String month;
    private int borrows;
}