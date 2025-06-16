package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
   SELECT b FROM Book b
   WHERE (:title IS NULL OR :title = '' OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
   """)
    Page<Book> searchBooks(@Param("title") String title, Pageable pageable);
}
