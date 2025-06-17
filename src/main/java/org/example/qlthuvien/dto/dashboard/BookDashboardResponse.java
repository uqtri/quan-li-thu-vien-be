package org.example.qlthuvien.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class BookDashboardResponse {
    private int totalBooks;
    private int activeBorrows;
    private int overdueBooks;
    private List<MonthlyBorrows> monthlyBorrows;
    private List<CategoryDistribution> categoryDistribution;
}