package org.example.qlthuvien.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Transactional
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password_hash;

    private Date created_at = new Date();

    @Enumerated(EnumType.STRING)
    private ROLE role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<BorrowedBook> lendings;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Reservation> reservations;
    private Integer xp;

    private String image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Wishlist> wishlists;

    @PrePersist
    void onCreate() {
        this.role = ROLE.USER;
        this.xp = 0;
        this.image = "";
    }
}

