package org.example.qlthuvien.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservation_id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name="book_item_id")
    private BookItem bookItem;

    private LocalDateTime reservationDate;

    private boolean returned;

    @PrePersist
    public void onCreate() {
        this.reservationDate = LocalDateTime.now();
        this.returned = false;
    }
}
