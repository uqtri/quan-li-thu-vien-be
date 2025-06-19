package org.example.qlthuvien.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="chatbot")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")

public class Chatbot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private boolean is_bot;

    private LocalDateTime created_at;

    @Column(columnDefinition = "TEXT")
    private String message;
    @PrePersist void onCreate() {
        this.created_at = LocalDateTime.now();
    }
}
