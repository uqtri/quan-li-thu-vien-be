package org.example.qlthuvien.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.entity.BorrowedBook;
import org.example.qlthuvien.entity.ROLE;
import org.example.qlthuvien.entity.Reservation;
import org.example.qlthuvien.entity.Wishlist;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String image;
    private Date created_at;

    private ROLE role;

    private List<BorrowedBook> lendings;
    private List<Reservation> reservations;

    private Integer xp;
    private List<Wishlist> wishlists;
}


