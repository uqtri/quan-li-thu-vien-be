package org.example.qlthuvien.repository;


import org.example.qlthuvien.entity.BookItem;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookItemRepository extends JpaRepository<BookItem, Long> {

    @Query("SELECT b FROM BookItem b WHERE b.book.id = :bookId")
    List<BookItem> findBookItemsByBookId(@Param("bookId") Long bookId);

}
