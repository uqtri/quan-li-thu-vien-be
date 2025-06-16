package org.example.qlthuvien.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qlthuvien.entity.BorrowedBook;
import org.example.qlthuvien.entity.ROLE;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse { private Long id;

    private String name;
    private String email;
    private String password_hash;

    private Date created_at;

    private ROLE role;

    private List<BorrowedBook> lendings;
}


