package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.dashboard.*;
import org.example.qlthuvien.entity.LendingStatus;
import org.example.qlthuvien.mapper.DashboardMapper;
import org.example.qlthuvien.repository.BookRepository;
import org.example.qlthuvien.repository.BorrowedBookRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookDashboardController {
    private final BookRepository bookRepository;
    private final BorrowedBookRepository borrowedBookRepository;
    private final DashboardMapper dashboardMapper;

    @GetMapping("/dashboard")
    public BookDashboardResponse getDashboard() {
        int totalBooks = (int) bookRepository.count();
        int activeBorrows = borrowedBookRepository.countByStatus(LendingStatus.BORROWED);
        int overdueBooks = borrowedBookRepository.countByStatus(LendingStatus.OVERDUE);

        List<MonthlyBorrows> monthly = generateMonthlyBorrows();
        List<CategoryDistribution> categories = generateCategoryDistribution();

        return new BookDashboardResponse(totalBooks, activeBorrows, overdueBooks, monthly, categories);
    }

    private List<MonthlyBorrows> generateMonthlyBorrows() {
        List<Object[]> results = borrowedBookRepository.countMonthlyBorrows();
        Map<Integer, Integer> monthMap = results.stream()
                .collect(Collectors.toMap(
                        r -> ((Number) r[0]).intValue(),
                        r -> ((Number) r[1]).intValue()
                ));

        List<MonthlyBorrows> full = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            String monthName = Month.of(i).name().substring(0, 1).toUpperCase() + Month.of(i).name().substring(1, 3).toLowerCase();
            full.add(new MonthlyBorrows(monthName, monthMap.getOrDefault(i, 0)));
        }
        return full;
    }

    private List<CategoryDistribution> generateCategoryDistribution() {
        return bookRepository.countBooksByCatalog().stream()
                .map(obj -> dashboardMapper.objectToCategoryDistribution(obj))
                .collect(Collectors.toList());
    }
}