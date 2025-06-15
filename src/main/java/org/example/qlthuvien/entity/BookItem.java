package org.example.qlthuvien.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private STATUS status;
    private Date created_at;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

 }

enum STATUS {
    AVAILABLE,
    Borrowed,
}