package org.example.qlthuvien.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password_hash;

    private Date created_at;

    @Enumerated(EnumType.STRING)
    private ROLE role;

    @OneToMany(mappedBy = "user")
    private List<BorrowedBook> lendings;

}

enum ROLE{
    ADMIN,
    USER,
    LIBRARIAN
}

