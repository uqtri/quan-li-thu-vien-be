package org.example.qlthuvien.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "book_item"})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservation_id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @OneToOne
    @JoinColumn(name="book_item_id")
    private BookItem bookItem;

    @ManyToOne
    @JoinColumn(name="book_id")
    @JsonIgnore
    private  Book book;
    private LocalDateTime reservationDate;

    private boolean returned;

    @PrePersist
    public void onCreate() {
        this.reservationDate = LocalDateTime.now();
    }

}
