package org.example.qlthuvien.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "Book")
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng ID
    private Long id;
    private String title;
    private String author;
    private String description;
    private String image;
    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    @OneToMany(mappedBy = "book")
    private List<BookItem> bookItems;

}
