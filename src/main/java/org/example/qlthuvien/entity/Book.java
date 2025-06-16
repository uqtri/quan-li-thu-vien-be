package org.example.qlthuvien.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "book")
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
    private double avg_rating;
    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<BookItem> bookItems;

    @PrePersist void onCreate() {
        this.avg_rating = 0;
    }
}
