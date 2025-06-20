package org.example.qlthuvien.repository;

import jakarta.transaction.Transactional;
import org.example.qlthuvien.entity.BookItem;
import org.example.qlthuvien.entity.Reservation;
import org.example.qlthuvien.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByBookItemId(Long bookItemId);
    @Modifying
    @Transactional
    @Query("""
    UPDATE Reservation r
    SET r.returned = true
    WHERE r.bookItem.book.id = :bookId AND r.returned = false
""")
    void updateReturnedByBookItemBookId(@Param("bookId") Long bookId);

    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.returned = false WHERE r.bookItem.book.id = :bookId AND r.returned = true")
    void updateReturnedFalseByBookItemBookId(@Param("bookId") Long bookId);

    boolean existsByUserAndBookItem(User user, BookItem bookItem);

}
