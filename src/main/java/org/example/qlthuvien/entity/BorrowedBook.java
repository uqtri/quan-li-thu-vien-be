package org.example.qlthuvien.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrowed_book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "book_item"})
public class BorrowedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_items_id")
    private BookItem book_item;

    private LocalDateTime borrow_date;

    private LocalDateTime return_date;

    @Enumerated(EnumType.STRING)
    private LendingStatus status;

    @PrePersist
    public void onCreate() {
        this.borrow_date = LocalDateTime.now();
        this.status = LendingStatus.BORROWED;
    }

}
