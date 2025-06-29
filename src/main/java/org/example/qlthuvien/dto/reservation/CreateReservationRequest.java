package org.example.qlthuvien.dto.reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationRequest {
    private Long user_id;
    private Long book_item_id;
    private Long book_id;
}
