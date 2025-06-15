package org.example.qlthuvien.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    private boolean seen;
    private String message;
    private LocalDateTime created_at;
}
