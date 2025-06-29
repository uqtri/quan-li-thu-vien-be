package org.example.qlthuvien.repository;

import jakarta.transaction.Transactional;
import org.example.qlthuvien.entity.Book;
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
    List<Reservation> findByBookId(Long bookId); // sửa từ bookItemId

    @Modifying
    @Transactional
    @Query("""
        UPDATE Reservation r
        SET r.returned = true
        WHERE r.book.id = :bookId AND r.returned = false
    """)
    void updateReturnedByBookId(@Param("bookId") Long bookId);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Reservation r
        SET r.returned = false
        WHERE r.book.id = :bookId AND r.returned = true
    """)
    void updateReturnedFalseByBookId(@Param("bookId") Long bookId);

    boolean existsByUserAndBook(User user, Book book); // đổi BookItem thành Book
}

