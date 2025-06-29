package org.example.qlthuvien.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.qlthuvien.dto.bookitem.STATUS;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "book_item")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"book"})
public class BookItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Enumerated(EnumType.STRING)
    private STATUS status;

    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name="book_id")
    @JsonIgnoreProperties("bookItems")
    private Book book;

    @OneToOne(mappedBy = "book_item", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("book_item")
    private BorrowedBook borrowedBook;

    @OneToMany(mappedBy = "bookItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reservation> reservation;


    @PrePersist void onCreate() {
        this.created_at = LocalDateTime.now();
        this.status = STATUS.AVAILABLE;
    }
 }

