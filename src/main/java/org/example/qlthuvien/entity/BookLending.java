package org.example.qlthuvien.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BOOK_LENDING")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookLending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private String borrowDate;
    private String returnDate;

    @Enumerated(EnumType.STRING)
    private LendingStatus status;
}
enum LendingStatus {
    BORROWED,
    RETURNED,
    OVERDUE
}
