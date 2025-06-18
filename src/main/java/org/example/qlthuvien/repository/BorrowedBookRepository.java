package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.BorrowedBook;
import org.example.qlthuvien.entity.LendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    int countByStatus(LendingStatus status);

    @Query("SELECT MONTH(b.borrow_date), COUNT(b.id) FROM BorrowedBook b WHERE YEAR(b.borrow_date) = YEAR(CURRENT_DATE) GROUP BY MONTH(b.borrow_date)")
    List<Object[]> countMonthlyBorrows();
    int countByUserId(Long userId);

}
