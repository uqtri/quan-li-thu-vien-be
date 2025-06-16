package org.example.qlthuvien.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.dto.bookitem.STATUS;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "book_item")
@NoArgsConstructor
@AllArgsConstructor

public class BookItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Enumerated(EnumType.STRING)
    private STATUS status;

    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name="book_id")
    @JsonBackReference
    private Book book;

    @PrePersist void onCreate() {
        this.created_at = LocalDateTime.now();
        this.status = STATUS.AVAILABLE;
    }

 }

