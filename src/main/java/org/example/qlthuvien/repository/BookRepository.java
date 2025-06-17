package org.example.qlthuvien.repository;

import org.example.qlthuvien.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
   SELECT b FROM Book b
   WHERE (:title IS NULL OR :title = '' OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
   """)
    Page<Book> searchBooks(@Param("title") String title, Pageable pageable);

    @Query("SELECT c.name, COUNT(b.id) FROM Book b JOIN b.catalog c GROUP BY c.name")
    List<Object[]> countBooksByCatalog();
}
