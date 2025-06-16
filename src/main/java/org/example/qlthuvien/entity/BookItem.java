package org.example.qlthuvien.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.dto.bookitem.STATUS;

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

    private Date created_at;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

 }

