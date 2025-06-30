package org.example.qlthuvien.dto.reservation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.entity.Book;
import org.example.qlthuvien.entity.BookItem;
import org.example.qlthuvien.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private Long reservation_id;
    private User user;
    private BookItem bookItem;
    private Book book;
    private LocalDateTime reservationDate;
    private boolean returned;
}
